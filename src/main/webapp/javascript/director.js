$(document).ready
(
    function()
    {
        
        function appendPerson(id, name, surname, birthday, country)
        {
            $('#wrapper_add_director_window').hide
            (
                'fast',
                function()
                {
                    $('#director_table tbody').append(
                        '<tr><td class="button_container"><a href="/person/'+id+'#directors" class="show"></a></td>\n\
                        <td class="field_picture"><img src="/pictures/person/'+id+'/tiny.jpg" /></td>\n\
                        <td>'+name+'</td>\n\
                        <td>'+surname+'</td>\n\
                        <td class="field_country"><a href="/countries/'+country+'"><img src="/pictures/country/'+country+'/tiny.jpg" title="'+$('#form_person :selected').text()+'"></a></td>\n\
                        <td class="field_year">'+birthday+'</td></tr>'
                    ).hide().fadeIn('slow');
                    $('#wrapper_add_director_window option').removeAttr('selected');
                    $('.user_data input, .user_data select').removeAttr('disabled');
                    $('.user_data input').val('');
                    $('.loading').hide();
                }
            );
        }
        $('#form_person').change
        (
            function()
            {
                if ($('#form_person option:selected').val() != 0) {
                    $('.user_data input, .user_data select').attr('disabled', 'disabled');
                } else {
                    $('.user_data input, .user_data select').removeAttr('disabled');
                }
            }
        );
        $('#wrapper_add_director_window form').submit
        (
            function(event)
            {
                event.preventDefault();
                $('.loading').show();
                if ($('#form_person option:selected').val() == 0) {
                    var name = $('#form_name').val(),
                        surname  = $('#form_surname').val(),
                        birthday = $('#form_birthday').val(),
                        country  = $('#form_country :selected').val();
                    $.post
                    (
                        window.location.href+'/addnew',
                        {
                            name     : name,
                            surname  : surname,
                            birthday : birthday,
                            country  : country
                        },
                        function(response)
                        {
                            appendPerson(response, name, surname, birthday, country);
                        }
                    );
                } else {
                    var person_id = $('#form_person :selected').val();
                    $.post
                    (
                        window.location.href+'/add',
                        {
                            person_id : person_id
                        },
                        function(r)
                        {
                            appendPerson(
                                person_id,
                                $('#form_person :selected').data('name'),
                                $('#form_person :selected').data('surname'),
                                $('#form_person :selected').data('birthday'),
                                $('#form_person :selected').data('country')
                            );
                            $('#form_person :selected').remove();
                        }
                    );
                }
            }
        );
    }
);