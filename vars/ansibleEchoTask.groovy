// ansibleEchoTask.groovy
def call() {
    // Run Ansible Playbook using the echo_task.yml playbook
    ansiblePlaybook(
        playbook: 'ansible/echo_task.yml',    // Path to the playbook within the library
        inventory: 'ansible/inventory.ini',  // Path to the inventory file
        ansibleOptions: '-v'                 // Verbose output for debugging
    )
}
