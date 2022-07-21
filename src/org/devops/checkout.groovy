package org.devops

def MonoCheckOut(PathOfDir, GitName, GitPasswd, BranchName, ScoUrl, GerritRefsm) {
    def CodeDir = new File("${PathOfDir}")
    Boolean bool = CodeDir.isDirctory()
    if (bool) {
        retry('9999') {
            dir(path: '') {
            def cmd = "git fetch --all".execute()
            cmd.waitForOrKill()
            }
        }        
    } else {
        mkdir(dir:"${PathOfDir}")
        dir(path: '${PathOfDir}'){
            warnError(message: 'origin already exists') {
                sh """
                git remote add origin http://${GitName}:${GitPasswd}@rd.loongson.cn:8081/a/mono
                """
            }
            retry('9999') {
                //checkout([$class: 'GitSCM', branches: [[name: '*/loongarch64-dev-6.0']], extensions: [], userRemoteConfigs: [[credentialsId: 'gerrit', url: 'http://rd.loongson.cn:8081/a/mono']]])
                checkout([$class: 'GitSCM', branches: [[name: '*/${BranchName}']], extensions: [], userRemoteConfigs: [[credentialsId: 'gerrit', url: '${ScoUrl}']]])
            }
        }
    }
    dir(path: '${PathOfDir}') {
        retry('9999') {
            sh 'git pull origin ${GerritRefs}'
        }
    }

}

