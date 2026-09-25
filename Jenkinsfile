def runMaven(String arguments) {
    if (isUnix()) {
        sh "mvn ${arguments}"
    } else {
        bat "mvn ${arguments}"
    }
}

pipeline {
    agent {
        label 'qa-linux'
    }

    options {
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
        skipDefaultCheckout(true)
    }

    parameters {
        choice(name: 'ENV', choices: ['qa', 'staging', 'prod-smoke'], description: 'Execution environment.')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Local browser installed on the Jenkins agent.')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run the browser without a visible window.')
        choice(name: 'TEST_SUITE', choices: ['smoke', 'casa-quintero', 'regression', 'e2e', 'negative', 'sanity'], description: 'Cucumber suite tag to execute.')
        choice(name: 'EXECUTION_MODE', choices: ['local'], description: 'Only local execution is enabled in this baseline.')
        string(name: 'BASE_URL_OVERRIDE', defaultValue: '', trim: true, description: 'Optional HTTP(S) base URL override. Leave empty to use the environment file.')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment / Tool Info') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'java -version && mvn -version'
                    } else {
                        bat 'java -version && mvn -version'
                    }
                }
            }
        }

        stage('Framework Self Tests') {
            steps {
                script {
                    // Starts from a clean Maven target and does not open a browser.
                    runMaven('clean test -Dtest=ConfigResolverTest,ArtifactPathsTest')
                }
            }
        }

        stage('QA Selected Suite') {
            steps {
                script {
                    def cucumberTag = "@${params.TEST_SUITE}"
                    def arguments = "test -Dtest=RunCucumberTest -Denv=${params.ENV} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -DexecutionMode=${params.EXECUTION_MODE} -Dcucumber.filter.tags=${cucumberTag}"

                    // BASE_URL is an Execution Contract input; it is not concatenated into a shell command.
                    if (params.BASE_URL_OVERRIDE?.trim()) {
                        withEnv(["BASE_URL=${params.BASE_URL_OVERRIDE.trim()}"]) {
                            runMaven(arguments)
                        }
                    } else {
                        runMaven(arguments)
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/cucumber-reports/**,target/logs/**,target/screenshots/**'
        }
        success {
            echo 'QA pipeline completed successfully.'
        }
        failure {
            echo 'QA pipeline failed. Review JUnit results and archived evidence.'
        }
        cleanup {
            echo 'Cleanup complete. Maven clean establishes the next build baseline; evidence remains archived in Jenkins.'
        }
    }
}
