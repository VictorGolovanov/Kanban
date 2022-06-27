$(function(){
    //


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
                // window.alert('OK!')
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
                // window.alert('OK!')
                window.location.reload();
            }
        });
        return false;
    });

    //Getting assignee
    // $(document).on('click', '.assignee-link', function() {
    //     var link = $(this);
    //     var assigneeId = link.data('id');
    //     $.ajax({
    //         method: "GET",
    //         url: '/assignees/' + assigneeId,
    //         success: function(response) {
    //             var code = '<span>Name: ' + response.name + '</span>';
    //             link.parent().append(code);
    //         },
    //         error: function(response) {
    //             if (response.status == 404) {
    //                 alert('Not found!');
    //             }
    //         }
    //     });
    //     return false;
    // });

})