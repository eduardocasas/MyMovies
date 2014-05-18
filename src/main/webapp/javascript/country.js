$(document).ready
(
    function()
    {
        
        /* ------------ info ------------*/

        $('.table').stupidtable();

        function turnEditModeOff()
        {
            $('#remove_country_button, #edit_country_button').show();
            $('#cancel_edit_button, #save_edit_button').hide();
            $.each
            (
                $('.input_edit_mode'),
                function()
                {
                    var text = $(this).data('previous_value'),
                        container = $(this).parent();
                    $(this).remove();
                    container.text(text);
                }
            );
            $('#field_country span, .table tbody').show();
            $('#field_country select, .table tfoot').hide();
            $('#upload_picture_form, #remove_picture').hide();
        }
        function turnEditModeOn()
        {
            $('#remove_country_button, #edit_country_button').hide();
            $('#cancel_edit_button, #save_edit_button').show();
            $.each
            (
                $('.edit_field'),
                function()
                {
                    var text = $(this).text();
                    $(this).text('');
                    $(this).append('<input class="input_edit_mode" type="text" id="'+$(this).attr('class').split(' ')[0]+'" value="'+text+'" data-previous_value="'+text+'">');
                }
            );
            $('#upload_picture_form').show();
            if (!$('#picture').hasClass('hide')) {
                $('#remove_picture').show();    
            }
        }
        $("#upload_picture").change
        (
            function()
            {
                $(this).closest('form').submit();
                $('#remove_picture').show();
                $('#picture').removeClass('hide');
            }
        );
        $('#upload_picture_iframe').load
        (
            function()
            {
                if (!$('#picture').hasClass('hide')) {
                    var href = $('#picture').attr('src').split('?')[0];
                    $('#picture').attr('src', href+'?'+new Date().getTime()).hide().fadeIn();
                }
            }
        );
        $('#remove_picture').click
        (
            function()
            {
                $.post
                (
                    window.location.href+'/remove-picture',
                    { },
                    function()
                    {
                        $('#remove_picture').hide();
                        $('#picture').hide().addClass('hide');
                    }
                );
            }
        );
        $('#save_edit_button').click
        (
            function()
            {
                $.post
                (
                    window.location.href+'/edit',
                    { name : $('#field_name').val() },
                    function(r)
                    {
                        $.each
                        (
                            $('.input_edit_mode'),
                            function() 
                            {
                                $(this).data('previous_value', $(this).val());
                            }
                        );
                        turnEditModeOff();
                    }
                );
            }
        );
        $('#remove_yes').click
        (
            function()
            {
                $.post
                (
                    window.location.href+'/remove',
                    { },
                    function(r) { window.location = '/countries' }
                );
            }
        );
        $('#remove_no').click(function(){ $('#wrapper_remove_country_window').hide() });
        $('#cancel_edit_button').click(function(){ turnEditModeOff() });
        $('#edit_country_button').click(function(){ turnEditModeOn() });

        /* ------------ index ------------*/

        $('#wrapper_add_country_window form').submit
        (
            function(event)
            {
                event.preventDefault();
                $('.loading').show();
                var name   = $('#form_name').val();
                $.post
                (
                    window.location.href+'/add',
                    { name   : name },
                    function(response)
                    {
                        $('#wrapper_add_country_window').hide
                        (
                            'fast',
                            function()
                            {
                                $('#country_table tbody').append(
                                    '<tr><td class="button_container"><a href="/countries/'+response+'" class="show"></a></td>\n\
                                    <td class="field_country"></td>\n\
                                    <td>'+name+'</td>\n\
                                    <td class="field_number">0</td>\n\
                                    <td class="field_number">0</td></tr>'
                                ).hide().fadeIn('slow');
                                $('.loading').hide();
                            }
                        );
                    }
                );
            }
        );
    }
);