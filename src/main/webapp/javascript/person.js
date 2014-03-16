$(document).ready
(
    function()
    {
        
        /* ------------ info ------------*/
        
        var URL = window.location.href.split('#')[0]+'/';
        
        $('.movie_section').hide();
        $(window.location.hash).show();
        $('a[href="'+window.location.hash+'"]').addClass('selected');
        function turnEditModeOff()
        {
            $('#remove_person_button, #edit_person_button').show();
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
            $('#person_role, #upload_picture_form, #remove_picture').hide();
        }
        function turnEditModeOn()
        {
            $('#remove_person_button, #edit_person_button').hide();
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
            $('#field_country option[value='+$('#field_country span').data('country_id')+']').prop('selected', true);
            $.each
            (
                $('#director_movie_table tbody tr'),
                function()
                {
                    $('#director_movie_table input[value='+$(this).data('movie_id')+']').prop('checked', true);
                }
            );
            $.each
            (
                $('#actor_movie_table tbody tr'),
                function()
                {
                    $('#actor_movie_table input[value='+$(this).data('movie_id')+']').prop('checked', true);
                }
            );
            $('#field_country span, .table tbody').hide();
            $('#field_country select, .table tfoot, #person_role, #upload_picture_form').show();
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
                    URL+'remove-picture',
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
                var actor_movie = [],
                    director_movie = [];
                $('#actor_movie_table tbody tr').remove();
                $('#director_movie_table tbody tr').remove();
                $.each
                (
                    $('#actor_movie_table input:checked'),
                    function()
                    {
                        actor_movie.push($(this).val());
                        var tr = $(this).closest('tr').clone();
                        $('input', tr).remove();
                        $('#actor_movie_table tbody').append(tr);
                    }
                );
                $.each
                (
                    $('#director_movie_table input:checked'),
                    function()
                    {
                        director_movie.push($(this).val());
                        var tr = $(this).closest('tr').clone();
                        $('input', tr).remove();
                        $('#director_movie_table tbody').append(tr);
                    }
                );
                $.post
                (
                    URL+'edit',
                    {
                        name           : $('#field_name').val(),
                        surname        : $('#field_surname').val(),
                        year           : $('#field_birthday').val(),
                        country        : $('#field_country :selected').val(),
                        actor          : $('#actor_role').prop('checked') ? 1 : 0,
                        director       : $('#director_role').prop('checked') ? 1 : 0,
                        actor_movie    : actor_movie,
                        director_movie : director_movie
                    },
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
                        $('#field_country span').data('country_id', $('#field_country :selected').val());
                        $('#field_country span').text($('#field_country :selected').text());
                        $('#directors, #cast').hide();
                        if ($('#actor_role').prop('checked')) {
                            if (!$('#director_role').prop('checked') || window.location.hash == '#cast') {
                                $('#cast').show();
                            }
                            $('a[href="#cast"]').show();
                        } else {
                            $('#cast, a[href="#cast"]').hide();
                        }
                        if ($('#director_role').prop('checked')) {
                            if (!$('#actor_role').prop('checked') || window.location.hash == '#directors') {
                                $('#directors').show();
                            }
                            $('a[href="#directors"]').show();
                        } else {
                            $('a[href="#directors"]').hide();
                        }
                        turnEditModeOff();
                    }
                );
            }
        );
        $('#tabs a').click
        (
            function()
            {
                $('.movie_section').hide();
                $('#tabs a').removeClass('selected');
                $('a[href="'+$(this).attr('href')+'"]').addClass('selected');
                $($(this).attr('href')).fadeIn();
            }
        );
        $('#remove_yes').click
        (
            function()
            {
                $.post
                (
                    URL+'remove',
                    { },
                    function(r)
                    {
                        switch (window.location.hash) {
                            case '#directors':
                                window.location = '/directors';
                                break;
                            case '#cast':
                                window.location = '/cast';
                                break;
                        }
                    }
                );
            }
        );
        $('#remove_no').click
        (
            function()
            {
                $('#wrapper_remove_person_window').hide();
            }
        );
        $('#cancel_edit_button').click
        (
            function()
            {
                turnEditModeOff();
            }
        );
        $('#edit_person_button').click
        (
            function()
            {
                turnEditModeOn();
            }
        );
    }
);
        
        