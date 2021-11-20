#! /bin/sh

SETCOLOR_SUCCESS="echo -en \\033[1;32m"  
SETCOLOR_FAILURE="echo -en \\033[1;31m"
SETCOLOR_WARNING="echo -en \\033[1;33m"
SETCOLOR_NORMAL="echo -en \\033[0;39m"

LogSuccess()
{
        time=`date "+%D %T"`
		$SETCOLOR_SUCCESS
		echo "[$time] [INFO]: $*"
		$SETCOLOR_NORMAL
}

LogInfo()
{
        time=`date "+%D %T"`
		echo "[$time] [INFO]: $*"
        $SETCOLOR_NORMAL
}

LogError()
{
        time=`date "+%D %T"`
		$SETCOLOR_FAILURE
		echo "[$time] [ERROR]: $*"
		$SETCOLOR_NORMAL
}

LogWarn()
{
        time=`date "+%D %T"`
		$SETCOLOR_WARNING
        echo "[$time] [WARN]: $*"
		$SETCOLOR_NORMAL
}

# JAVA_HOME路径，默认取环境变量，如果不存在或者版本不是1.8，则尝试使用下面配置的
java_home=

# 应用名称
app_name="fom"

# 应用安装目录
app_home="/home/fom"

# 应用启动类
main_class="com.fom.demo.DemoBoot"

# 应用启动参数
JVM_OPTION="-Xms256m -Xmx256m -XX:MetaspaceSize=128m"
JVM_OPTION="$JVM_OPTION -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails"

# 开放JMX端口
#JVM_OPTION="$JVM_OPTION -Dcom.sun.management.jmxremote"
#JVM_OPTION="$JVM_OPTION -Dcom.sun.management.jmxremote.port=4091"
#JVM_OPTION="$JVM_OPTION -Dcom.sun.management.jmxremote.ssl=false"
#JVM_OPTION="$JVM_OPTION -Dcom.sun.management.jmxremote.authenticate=false"

# 开放远程debug端口
#JVM_OPTION="$JVM_OPTION -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4099,suspend=n"

# 必要配置项，如果数组中的配置项在当前环境变量，或者globe.common.conf中均没有值，则校验失败
required_configuration=(service_port)

# 安装文件拷贝，工作目录为当前安装包解压目录，默认会拷贝如下文件：
# bin/
# lib/
install_copy(){
	cp -rf config $app_home
	if [ -d cache ];then
        cp -rf cache $app_home
    fi
}

# 安装文件配置，拷贝文件完成后，会对$app_home的配置文件进行配置
# 当前工作目录为$app_home，但是建议使用绝对路径
install_config(){
    sed -i 's#<appender-ref ref="consoleAppender" />##g' $app_home/config/logback.xml
    
    sed -i 's#server.port=.*#server.port='$service_port'#g' $app_home/config/application.properties
}

# 卸载时备份操作，工作目录为安装包当前解压目录
uninstall_bak(){
    if [ -d $app_home/cache ];then
        cp -rf $app_home/cache .
    fi
}


sources(){
    # svn update
    git fetch --all
    git reset --hard origin/master 

    # 更新可能会覆盖文件可执行属性
    chmod 744 bin/*.sh
}

package(){
   # 获取pom.xml中的version
   version=`grep -B 4 packaging pom.xml | grep version | awk -F ">" '{print $2}' | awk -F "<" '{print $1}'`
   # 打包时间
   build_time=`date "+%Y-%m-%d %H:%M:%S"`
   # 代码版本
   #submit=`svn info|awk 'NR==9{print $4}'`
   submit=`git log -n 1 --pretty=oneline | awk '{print $1}'`

   sed -i 's/^version=.*/version="'"$version"'"/' bin/run.sh
   sed -i 's/^submit=.*/submit="'"$submit"'"/' bin/run.sh
   sed -i 's/^build_time=.*/build_time="'"$build_time"'"/' bin/run.sh
}

md5(){
    cd target
    tar=`ls | grep *.tar.gz`
    md5sum $tar > $tar.md5
}

case "$1" in
	sources)
		sources
		;;
	package)
	    package
		;;
	md5)
	    md5
	    ;;
	*)
esac
