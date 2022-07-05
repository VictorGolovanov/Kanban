$(function() {

    //show adding task form
    $('#show-add-task-form').click(function() {
        $('#add-task-form').css('display', 'flex');
    });

    //Closing adding task form
    $('#add-task-form').click(function(event) {
        if (event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Closing editing task form
    $('#change-task-form').click(function(event) {
        if (event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Closing assigning task form
    $('#assign-task-form').click(function(event) {
        if (event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //show adding assignee form
    $('#show-add-assignee-form').click(function() {
        $('#add-assignee-form').css('display', 'flex');
    });

    //Closing adding assignee form
    $('#add-assignee-form').click(function(event) {
        if (event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //show changing assignee form
    $('#change-assignee-form').click(function() {
        $('#change-assignee-form').css('display', 'flex');
    });

    //Closing changing assignee form
    $('#change-assignee-form').click(function(event) {
        if (event.target === this) {
            $(this).css('display', 'none');
        }
    });


    //board rendering
    $('#get-data').on('click', function() {
        $.ajax({
            method: 'GET',
            url: '/assignees/',
            success: function(response) {
                renderTaskList(response);
            }
        });
    });


    function renderTaskList(data) {
        const taskTable = document.getElementById('task-list');
        taskTable.innerHTML = '';
        const taskHeaders = document.getElementsByClassName('task-header').length;
        const taskHeadersLength = taskHeaders.length;
        data.forEach((el) => {
            const row = document.createElement('tr');
            row.classList.add('task-row');
            row.setAttribute('data-task-id', el.id);

            // assignee
            const assignee = document.createElement('td');
            assignee.classList.add('assignee');
            assignee.innerHTML = el.name === 'Null-Person' ? 'Anassigned tasks' : el.name;

            if (el.name !== 'Null-Person') {
                const editAssigneeButton = document.createElement('button')
                editAssigneeButton.classList.add("change-assignee")
                editAssigneeButton.innerHTML = "edit"
                editAssigneeButton.addEventListener('click', () => changeAssignee(el))

                const deleteAssigneeButton = document.createElement('button')
                deleteAssigneeButton.classList.add("delete-assignee")
                deleteAssigneeButton.innerHTML = "delete"
                deleteAssigneeButton.addEventListener('click', () => deleteAssignee(el))

                assignee.appendChild(editAssigneeButton)
                assignee.appendChild(deleteAssigneeButton)

            }

            row.appendChild(assignee);

            // tasks
            if (el.tasks.length < 1) {
                for (let i = 0; i < taskHeadersLength; i++) {
                    const empty = document.createElement('td');
                    empty.classList.add('empty-cell');
                    row.appendChild(empty);
                }
            } else {
                const taskStatuses = ['TODO', 'INPROGRESS', 'COMPLETE'];
                const groupedByStatus = {};
                Array.from(taskHeaders).forEach(el => groupedByStatus[el.getAttribute('data-sort-by')] = []);

                el.tasks.forEach((t) => {
                    if (t == null) return;
                    if (groupedByStatus[t.status] == null) groupedByStatus[t.status] = [];
                    groupedByStatus[t.status].push(t);
                });

                taskStatuses.forEach(status => {
                    const td = document.createElement('td');
                    td.setAttribute('data-task-status', status);
                    if (groupedByStatus[status] != null) {
                        groupedByStatus[status].forEach((t, i) => {
                            const taskWrapper = document.createElement('div')
                            taskWrapper.classList.add('task-wrapper')
                            taskWrapper.setAttribute('data-task-id', status + t.id)
                            const taskName = document.createElement('div')
                            taskName.classList.add('task-name')
                            taskName.innerHTML = t.taskName

                            const taskDescription = document.createElement('div')
                            taskDescription.classList.add('task-description')
                            taskDescription.innerHTML = t.taskDescription;

                            const changeButton = document.createElement('button')
                            changeButton.classList.add("change-task")
                            changeButton.innerHTML = "edit"
                            changeButton.addEventListener('click', () => changeTask(t))

                            const assignButton = document.createElement('button')
                            assignButton.classList.add("assign-task")
                            assignButton.innerHTML = "assign"
                            assignButton.addEventListener('click', () => assignTask(t))



                            const deleteButton = document.createElement('button')
                            deleteButton.classList.add("delete-task")
                            deleteButton.innerHTML = "delete"
                            deleteButton.addEventListener('click', () => deleteTask(t))

                            taskWrapper.appendChild(taskName)
                            taskWrapper.appendChild(taskDescription)
                            taskWrapper.appendChild(changeButton)
                            taskWrapper.appendChild(assignButton)
                            taskWrapper.appendChild(deleteButton)


                            td.appendChild(taskWrapper)
                        });
                    }

                    row.appendChild(td);
                });
            }
            taskTable.append(row);
        });
    }

    //Add task
    $('#save-task').click(function() {
        var data = $('#add-task-form form').serialize();
        console.log(data)
        $.ajax({
            method: 'POST',
            url: '/tasks/',
            data: data,
            success: function(response) {
                $('#add-task-form').css('display', 'none');
                var task = {};
                task.id = response;
                window.alert('Task with id ' + task.id + ' added')
                var dataArray = $('#add-task-form form').serializeArray();
                window.location.reload();
            }
        });
        return false;
    });

    //Add assignee
    $('#save-assignee').click(function() {
        var data = $('#add-assignee-form form').serialize();
        console.log(data)
        $.ajax({
            method: 'POST',
            url: '/assignees/',
            data: data,
            success: function(response) {
                $('#add-assignee-form').css('display', 'none');
                var assignee = {};
                assignee.id = response
                window.alert('Assignee with id ' + assignee.id + ' added')
                window.location.reload();
            }
        });
        return false;
    });


    function changeTask(t) {       
        $('#change-task-form input[name="taskName"]').val(t.taskName)
        $('#change-task-form input[name="taskDescription"]').val(t.taskDescription)
        $('#change-task-form input[name="taskStatus"]').val(t.status)

        const form = $('#change-task-form');
        form.css({display: 'flex'});

        var taskId = t.id;
        console.log(taskId)
        $(document).on('click', '#save-changed-task', function() {
            var result = confirm('Are you sure? Your data will be overwriten.');
            if (result) {
                var data = $('#change-task-form form').serialize();
                console.log(data)
                $.ajax({
                    method: 'POST',
                    url: '/tasks/' + taskId,
                    data: data,
                    success: function(response) {
                        $('#change-task-form').css('display', 'none');
                    }
                });
            }
            return false;
        });
    }

    function assignTask(t) {
        const form = $('#assign-task-form')
        form.css({display: 'flex'})

        $(document).on('click', '#save-new-assignee', function() {
            var result = confirm('Are you sure? You are going to reassign this task.')
            if (result) {
                var data = $('#assign-task-form form').serialize();
                console.log(data)

                var assigneeName = data.replace('assigneeName=', '')
                console.log(assigneeName)

                $.ajax({
                    method: 'GET',
                    url: '/assignees/names/' + assigneeName,
                    success: function(response) {
                        $('#assign-task-form').css('display', 'none')
                        var assigneeId = response.id
                        var taskId = t.id
                        $.ajax({
                            method: 'POST',
                            url: '/assignees/' + assigneeId + '/' + taskId,
                            success: function(response) {
                                window.location.reload()
                            }
                        })
                    }
                })
            }

            return false;
        });
    }

    // probably is is not good practice
    function deleteTask(t) {
        var result = confirm('Are you sure? You are going to delete this task forever.')
        if (result) {
            $.ajax({
                method: 'DELETE',
                url: '/tasks/' + t.id,
                success: function(response) {
                    window.location.reload()
                }
            })
        }
    }

    function changeAssignee(el) {
        const form = $('#change-assignee-form')
        form.css({display: 'flex'})
        var assigneeId = el.id
        $(document).on('click', '#save-changed-assignee', function() {
            var result = confirm('Are you sure? Your data will be overwriten.')
            if (result) {
                var data = $('#change-assignee-form form').serialize()
                console.log(data)
                $.ajax({
                    method: 'POST',
                    url: '/assignees/name/' + assigneeId,
                    data: data,
                    success: function(response) {
                        form.css({display: 'none'})
                    }
                })
            }
            return false
        })
    }

    // probably is is not good practice too
    function deleteAssignee(el) {
        var result = confirm('Are you sure? You are going to delete assignee forever.')
        if (result) {
            $.ajax({
                method: 'DELETE',
                url: '/assignees/' + el.id,
                success: function(response) {
                    window.location.reload()
                }
            })
        }
    }

})