pipeline {
    agent { label 'Agent' }

    environment {
        JAVA_HOME = '/opt/jdks/21'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        GRADLE_USER_HOME = '/home/jenkins/.gradle'
    }

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    parameters {
        choice(name: 'RELEASE_TYPE', choices: ['NO_DEPLOY', 'STABLE', 'BETA', 'ALPHA'], description: 'Choose NO_DEPLOY to build without publishing.')
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                    set -eu
                    # Remove stale outputs from loaders disabled since the previous build.
                    rm -rf fabric/build/libs forge/build/libs neoforge/build/libs
                    # ForgeGradle generates Minecraft dependencies during configuration.
                    # Build separately so clean cannot delete them before compilation.
                    ./gradlew clean --no-daemon
                    ./gradlew build --no-daemon
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

        stage('Publish') {
            when {
                expression {
                    params.RELEASE_TYPE in ['STABLE', 'BETA', 'ALPHA']
                }
            }
            steps {
                echo "Publishing release type: ${params.RELEASE_TYPE}"
                withCredentials([
                    string(credentialsId: 'minecraftcapes-modrinth-token', variable: 'MODRINTH_TOKEN'),
                    string(credentialsId: 'minecraftcapes-curseforge-token', variable: 'CURSEFORGE_TOKEN')
                ]) {
                    sh './gradlew publishMods --no-daemon'
                }
            }
        }
    }
}
