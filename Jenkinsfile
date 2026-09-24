pipeline {
    agent { label 'Agent' }

    environment {
        JAVA_HOME = '/opt/jdks/25'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        GRADLE_USER_HOME = '/home/jenkins/.gradle'
    }

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                    set -eu
                    # Remove stale outputs from loaders disabled since the previous build.
                    rm -rf fabric/build/libs forge/build/libs neoforge/build/libs
                    ./gradlew clean build --no-daemon
                '''
            }
        }

        stage('Deploy artifacts') {
            steps {
                sh '''
                    set -eu

                    rm -rf jenkins-artifacts
                    mkdir -p jenkins-artifacts

                    output_count=0
                    for libs in fabric/build/libs forge/build/libs neoforge/build/libs; do
                        [ -d "$libs" ] || continue
                        output_count=$((output_count + 1))

                        jar_count=0
                        selected_jar=''
                        for jar in "$libs"/*.jar; do
                            [ -f "$jar" ] || continue
                            case "$jar" in
                                *-sources.jar|*-javadoc.jar|*-dev.jar|*-shadow.jar|*-all.jar) continue ;;
                            esac
                            jar_count=$((jar_count + 1))
                            selected_jar="$jar"
                        done

                        if [ "$jar_count" -ne 1 ]; then
                            echo "Expected exactly one deployable JAR in $libs, found $jar_count."
                            exit 1
                        fi

                        target="jenkins-artifacts/$(basename "$selected_jar")"
                        if [ -e "$target" ]; then
                            echo "Duplicate artifact filename: $target"
                            exit 1
                        fi
                        cp -v "$selected_jar" "$target"
                    done

                    if [ "$output_count" -eq 0 ]; then
                        echo "No loader artifact directories were produced."
                        exit 1
                    fi
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
