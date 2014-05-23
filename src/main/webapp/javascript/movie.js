$(document).ready
(
    function()
    {
        
        /* ------------ info ------------*/

        if (typeof($.stupidtable) != 'undefined') {
            $('.table').stupidtable();
        }

        function turnEditModeOff()
        {
            $('#remove_movie_button, #edit_movie_button').show();
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
            $('#remove_movie_button, #edit_movie_button').hide();
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
                    $('#director_movie_table input[value='+$(this).data('director_id')+']').prop('checked', true);
                }
            );
            $.each
            (
                $('#actor_movie_table tbody tr'),
                function()
                {
                    $('#actor_movie_table input[value='+$(this).data('actor_id')+']').prop('checked', true);
                }
            );
            $('#field_country span, .table tbody').hide();
            $('#field_country select, .table tfoot, #upload_picture_form').show();
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
                    window.location.href+'/edit',
                    {
                        title          : $('#field_name').val(),
                        score          : $('#field_score').val(),
                        year           : $('#field_birthday').val(),
                        country        : $('#field_country :selected').val(),
                        actor_movie    : actor_movie,
                        director_movie : director_movie
                    },
                    function(r)
                    {
                        $.each
                        (
                            $('.input_edit_mode'),
                            function() { $(this).data('previous_value', $(this).val()) }
                        );
                        $('#field_country span').data('country_id', $('#field_country :selected').val());
                        $('#field_country span').text($('#field_country :selected').text());
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
                    function(r) { window.location = '/movies' }
                );
            }
        );
        $('#remove_no').click(function(){ $('#wrapper_remove_movie_window').hide() });
        $('#cancel_edit_button').click(function(){ turnEditModeOff() });
        $('#edit_movie_button').click(function(){ turnEditModeOn() });

        /* ------------ index ------------*/

        $('#wrapper_add_movie_window form').submit
        (
            function(event)
            {
                event.preventDefault();
                $('.loading').show();
                var title   = $('#form_title').val(),
                    year    = $('#form_year').val(),
                    score   = $('#form_score').val(),
                    country = $('#form_country :selected').val();
                $.post
                (
                    window.location.href+'/add',
                    {
                        title   : title,
                        year    : year,
                        score   : score,
                        country : country
                    },
                    function(response)
                    {
                        $('#wrapper_add_movie_window').hide
                        (
                            'fast',
                            function()
                            {
                                $('#movie_table tbody').append(
                                    '<tr><td class="button_container"><a href="/movies/'+response+'" class="show"></a></td>\n\
                                    <td  class="field_picture"></td>\n\
                                    <td>'+title+'</td>\n\
                                    <td class="field_country"><a href="/countries/'+country+'"><img src="/pictures/country/'+country+'/tiny.jpg" title="'+$('#form_country :selected').text()+'"></a></td>\n\
                                    <td  class="field_year">'+year+'</td>\n\
                                    <td class="field_score">'+score+'</td></tr>'
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