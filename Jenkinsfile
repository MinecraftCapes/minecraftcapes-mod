pipeline {
    agent {
        docker {
            image 'mcr.microsoft.com/openjdk/jdk:21-ubuntu'
            args '--user root:root'
            reuseNode true
        }
    }

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew build --no-daemon'
            }
        }

        stage('Deploy artifacts') {
            steps {
                sh '''
                    set -eu

                    rm -rf jenkins-artifacts
                    mkdir -p jenkins-artifacts

                    for launcher in fabric forge neoforge; do
                        jar_count="$(find "$launcher/build/libs" -maxdepth 1 -type f \
                            -name '*.jar' \
                            ! -name '*-sources.jar' \
                            ! -name '*-javadoc.jar' \
                            ! -name '*-dev.jar' \
                            ! -name '*-shadow.jar' \
                            ! -name '*-all.jar' | wc -l | tr -d ' ')"

                        if [ "$jar_count" -ne 1 ]; then
                            echo "Expected exactly one deployable JAR for $launcher, found $jar_count."
                            find "$launcher/build/libs" -maxdepth 1 -type f -name '*.jar' -print
                            exit 1
                        fi

                        find "$launcher/build/libs" -maxdepth 1 -type f \
                            -name '*.jar' \
                            ! -name '*-sources.jar' \
                            ! -name '*-javadoc.jar' \
                            ! -name '*-dev.jar' \
                            ! -name '*-shadow.jar' \
                            ! -name '*-all.jar' \
                            -exec cp -v '{}' jenkins-artifacts/ ';'
                    done
                '''

                archiveArtifacts(
                    artifacts: 'jenkins-artifacts/*.jar',
                    fingerprint: true,
                    onlyIfSuccessful: true
                )
            }
        }
    }
}
