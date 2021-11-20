#!/bin/sh

if [ ! -f ./bin/setenv.sh ];then
   LogError "install terminated unexpectedly: can not find setenv.sh."
   exit
else
   . ./bin/setenv.sh
fi

dir_base=`pwd`

check_exist(){
    v_new=`sed -n '/^version=.*/p' bin/run.sh 2>/dev/null | sed 's/version=//g' 2>/dev/null`
    if [ -z $v_new ];then
        LogError "install terminated unexpectedly: unknown version, maybe the installation package is damaged."
        exit 0
    fi 
	LogInfo "prepare to install $app_name, version: $v_new"

    if [ -d $app_home ];then
        v_old=`sed -n '/^version=.*/p' $app_home/bin/run.sh 2>/dev/null | sed 's/version=//g' 2>/dev/null`
		if [ -z $v_old ];then
		   LogError "install terminated unexpectedly: $app_name was already installed, but version is unknown."
		   exit 0
		fi
		
		compare=`awk -v a=$v_old -v b=$v_new 'BEGIN{print(a>=b)?"0":"1"}'`
		if [ $compare = "0" ]; then 
		   LogWarn "$app_name version $v_old was already installed, install cancelled."
           exit 0
        fi    
        
        LogInfo "$app_name version $v_old was installed, and will update to verion $v_new."
        uninstall
    fi
}

check_configuration(){
	if [ ! -f ./bin/env.conf ];then
        LogError "install terminated unexpectedly: config failed, env.conf not exist."
        exit
    fi
    
    dos2unix bin/env.conf
    file=temp.conf.`date +%s`
    cat bin/env.conf | sed '/^#.*/d' | sed '/^[ \t ]*$/d' | grep = | sed 's/[ \t]*=[ \t]*/=/' | awk -F "=" '{sub(/\./, "_", $1); printf("%s=%s"\n), $1, $2}' > $file
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
       LogError "install terminated unexpectedly: config failed, required configuration{ ${required_configuration[@]} }."
       exit
    fi
}

install(){
    mkdir -p $app_home/log
    
    copyfailed=0	
    
    cp -rf lib bin $app_home
    copyfailed=$(expr $copyfailed + $?)
    
    install_copy
    copyfailed=$(expr $copyfailed + $?)
    
    if [ ! $copyfailed == 0 ];then
		LogError "install terminated unexpectedly: copy failed."
	    exit
    fi
    
	install_time=`date "+%Y-%m-%d %H:%M:%S"`
	sed -i 's/^install_time=.*/install_time="'"$install_time"'"/' $app_home/bin/run.sh
	
	cd $app_home
	install_config
    if [ ! $? == 0 ];then
       LogError "install terminated unexpectedly: config failed."
       if [ ! -z $app_home ] && [ ! $app_home = "/" ];then
          rm -rf $app_home
       fi
       exit
    fi
	
	find $app_home -type f -name "*.sh" -exec chmod 744 {} \;
	find $app_home -type f -name "*.sh" -exec dos2unix {} \; 2>/dev/null
	find $app_home -type f -name "*.xml" -exec dos2unix {} \; 2>/dev/null
	find $app_home -type f -name "*.properties" -exec dos2unix {} \; 2>/dev/null
	LogSuccess "$app_name install success."
    
	cd $app_home
	sh bin/run.sh start
}

uninstall(){
    pids=`ps -ef --width 4096|grep -w $main_class |grep -v grep |awk '{print $2}'`
	
	if [ ! -z "$pids" ];then
		for	pid in $pids
	    do
		    installed_home=`pwdx $pid|awk -F ' ' '{print $2}'`
		    if [ "$installed_home" = "$app_home" ];then  
               kill -15 $pid
               sleep 2
               if kill -0 $pid > /dev/null 2>&1; then
                   kill -9 $pid
               fi
		       LogSuccess "$app_name stoped[pid=$pid]."
            fi
        done  
	fi
	
	if [ -d $app_home ];then
	   if [ ! -z $app_home ] && [ ! $app_home = "/" ];then
	   	  uninstall_bak
          rm -rf $app_home
          if [ ! $? == 0 ];then
              LogError "uninstall terminated unexpectedly: rm failed."
              exit
          fi
       fi
       LogSuccess "$app_name cleared[home=$app_home]."
	fi
	LogSuccess "$app_name uninstall success."
}

case "$1" in
	install)
        check_exist
        check_configuration
        install
		;;
	uninstall)
	    uninstall
		;;
	reinstall)
	    uninstall
		check_configuration
	    install
	    ;;
	*)
	LogError $"usage: $0 {install|uninstall|reinstall}"
	exit 1
esac
exit 0
