#! /bin/sh

export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8

if [ ! -f ./bin/setenv.sh ];then
	if [ ! -f ./setenv.sh ];then
		LogError "can not find setenv.sh."
		exit
    else
    	. ./setenv.sh
    fi
else
   . ./bin/setenv.sh
fi

pwd=$(cd "$(dirname "$0")"; pwd)
current_pwd=`pwd`

version=
submit=
build_time=
install_time="not installed"
start_time="not started"

version(){     
	  $SETCOLOR_SUCCESS
      echo "version: $version($submit)"
      $SETCOLOR_NORMAL
      echo "build time: $build_time"
	  echo "install time: $install_time"
}

check_jre(){	
	JAVA_VERSION=`java -version 2>&1 | grep version | awk -F '"' '{print $2}' 2>/dev/null`
	if [[ $JAVA_VERSION != 1.8.* ]];then
       export JAVA_HOME=$java_home
       export PATH=$JAVA_HOME/bin:$PATH
    fi
    
    JAVA_VERSION=`java -version 2>&1 | grep version | awk -F '"' '{print $2}' 2>/dev/null`
	if [[ $JAVA_VERSION != 1.8.* ]];then
       LogError "start failed: JRE version must be 1.8.x."
	   exit 1
    fi
    java -version
}

start(){  
    if [[ $current_pwd != $app_home* ]];then
	   LogError "[run.sh start] can only run in $app_name home: $app_home."
	   exit   
	fi
	
	LIBPATH=$app_home
	LINE=`find $app_home/lib -depth -name "*.jar"`
	for LOOP in $LINE
	do
		LIBPATH=$LIBPATH:$LOOP
	done
	
	s_time=`date "+%Y-%m-%d %H:%M:%S"`
	echo "################################################################### start: $s_time">>$app_home/boot.log  
    exec java $JVM_OPTION -Duser.dir=$app_home -cp "$app_name:$LIBPATH" $main_class $1 $2 $3 $4 $5 $6 $7 $8 1>>$app_home/boot.log 2>&1  &
	if [ ! $? == 0 ];then
	   LogWarn "$app_name start failed, see details in boot.log"
       exit
    fi 
	
	sleep 2
	pid=`ps -ef --width 4096|grep -w $main_class |grep -v grep |awk '{print $2}'`
	if [ ! -z "$pid" ];then
	   sed -i 's/^start_time=.*/start_time="'"$s_time"'"/' $app_home/bin/run.sh
	   LogSuccess "$app_name start success[pid=$pid]."
	else
	   LogWarn "$app_name state unknown, see details in boot.log"
	   exit
	fi
}

stop(){
    if [[ $current_pwd != $app_home* ]];then
	   LogError "[run.sh stop] can only run in $app_name home: $app_home."
	   exit   
	fi
	
    pids=`ps -ef --width 4096|grep -w $main_class |grep -v grep |awk '{print $2}'`
	if [ -z "$pids" ];then
	   LogWarn "$app_name was not running."
	   exit
	fi
	
	s_time=`date "+%Y-%m-%d %H:%M:%S"`
	echo "################################################################### stop: $s_time">>$app_home/boot.log
	for	pid in $pids
	do
        kill -15 $pid
        sleep 2
        if kill -0 $pid > /dev/null 2>&1; then
           kill -9 $pid
        fi
		LogSuccess "$app_name stoped[pid=$pid]."
    done
}

restart(){
    if [[ $current_pwd != $app_home* ]];then
	   LogError "[run.sh restart] can only run in $app_name home: $app_home."
	   exit   
	fi
	
    pids=`ps -ef --width 4096|grep -w $main_class |grep -v grep |awk '{print $2}'`
	if [ ! -z "$pids" ];then
	   s_time=`date "+%Y-%m-%d %H:%M:%S"`
	   echo "################################################################### stop: $s_time">>$app_home/boot.log
	   for	pid in $pids
	   do
           kill -15 $pid
           sleep 1
           if kill -0 $pid > /dev/null 2>&1; then
              kill -9 $pid
           fi
		   LogSuccess "$app_name stoped[pid=$pid]."
       done
	fi

    start
}

status(){	
    pids=`ps -ef --width 4096|grep -w $main_class |grep -v grep |awk '{print $2}'`
	if [ ! -z "$pids" ];then
		$SETCOLOR_SUCCESS
	    echo "Running[pid=$pids]."	
	    $SETCOLOR_NORMAL
    else
    	$SETCOLOR_FAILURE
        echo "Dead."
        $SETCOLOR_NORMAL
    fi 
	echo "last start time: $start_time"
}

set_configuration(){
	if [[ $current_pwd != $app_home* ]];then
	   LogError "]run.sh restart] can only run in $app_name home: $app_home."
	   exit   
	fi
	
	if [ ! -f $app_home/bin/env.conf ];then
        LogError "config failed, env.conf not exist."
        exit
    fi
    
    file=temp.conf.`date +%s`
    cat $app_home/bin/env.conf | sed '/^#.*/d' | sed '/^[ \t ]*$/d' | grep = | sed 's/[ \t]*=[ \t]*/=/' > $file
    while read line
    do
         key=`echo $line | awk -F "=" '{print $1}'`
         if [ -z $key ]; then
            continue
         fi
         
         value=`echo $line | awk -F "=" '{print $2}'`
         if [ -z $value ]; then
             for i in ${!required_configuration[@]}
                do
                    if [ "$key" == "${required_configuration[$i]}" ]; then
                       v=`eval echo '$'"$key"`
                       if [ -z $v ]; then
                          LogError "install terminated unexpectedly: config failed, $key can not be empty."
                          rm -f $file
                          exit
                       else
                       	  unset required_configuration[$i]
                       fi
                    fi
                done
         else
         	  for i in ${!required_configuration[@]}
                do
                    if [ "$key" == "${required_configuration[$i]}" ]; then
                       unset required_configuration[$i]
                    fi
                done
         	  
              v=`eval echo '$'"$key"`
              if [ ! -z $v ]; then
                  LogInfo "$key was already set in environment, value is $v"
              else
                export $key=$value
              fi
         fi
    done < $file 
    rm -f $file
    
    if [ ! ${#required_configuration[*]} == 0 ];then
       LogError "config failed, required configuration{ ${required_configuration[@]} }."
       exit
    fi
    
    install_config
    
    if [ $? == 0 ];then
       $SETCOLOR_SUCCESS
	   echo "config completed."
	   $SETCOLOR_NORMAL
    else
       $SETCOLOR_FAILURE
       "config failed."
       $SETCOLOR_NORMAL
    fi
}

case "$1" in
    start)
        check_jre
    	start $2 $3 $4 $5 $6 $7 $8 $9
		;;
    stop)
    	stop $2 $3 $4 $5 $6 $7 $8 $9
		;;
    restart)
    	restart $2 $3 $4 $5 $6 $7 $8 $9
		;;
    reconfig)
        set_configuration
		;;
    version)
        version
		;;
    status)
		status
		;;
    *)
  LogError $"usage: $0 {start|stop|restart|reconfig|status|version}"
    exit 1
esac
exit 0