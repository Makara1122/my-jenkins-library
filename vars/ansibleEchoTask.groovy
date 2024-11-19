def call() {
    // Fetch playbook from resources and save to workspace
    writeFile file: 'ansible/echo_task.yml', text: libraryResource('ansible/echo_task.yml')
    
    // Fetch inventory from resources and save to workspace
    writeFile file: 'ansible/inventory.ini', text: libraryResource('ansible/inventory.ini')
    
    // Run the Ansible Playbook
    ansiblePlaybook(
        playbook: 'ansible/echo_task.yml',    // Path in the workspace
        inventory: 'ansible/inventory.ini',  // Path in the workspace
        ansibleOptions: '-v'                 // Verbose output for debugging
    )
}
