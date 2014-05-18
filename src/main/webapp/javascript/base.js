function InputFileStyle(element) {
    if (element.length) {
        element.after('<button class="upload_button">Subir</button>');
        $('.upload_button').click
        (
            function(event)
            {
                event.preventDefault();
                $(this).prev($('input[type=file]')).click();
            }
        );     
    }   
}

/* ----- Ajax Forms ----- */

$.fn.serializeJSON = function()
{

    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;

    return json;
};

$(document).ready
(
    function()
    {
        
        /* ----- Header ----- */
        
        var main_header       = $('#main_header_wrapper'),
            scroll_top_button = $('#scroll_top_button');
    
        main_header.hover
        (
            function()
            {
                main_header.removeClass('onscroll');
            },
            function()
            {
                if ($(document).scrollTop() != 0) {
                    main_header.addClass('onscroll');
                }
            }
        );
        $(window).scroll
        (
            function()
            {
                main_header.add(scroll_top_button).addClass('onscroll');
                if ($(document).scrollTop() == 0) {
                    main_header.add(scroll_top_button).removeClass('onscroll');
                }
            }
        );
        scroll_top_button.click
        (
            function(event)
            {
                event.preventDefault();
                $('html, body').animate({ scrollTop: 0 }, 600);
            }
        );
        $('#display_configuration_menu_button').click(function(){ $('#configuration_menu').toggle() });

        /* ----- Fadein Windows ----- */

        $('.display_window').click
        (
            function(event)
            {
                event.preventDefault();
                $('#'+$(this).data('window')).show();
            }
        );
        $('.wrapper_fadein_window .close').click
        (
            function(event)
            {
                event.preventDefault();
                $(this).closest('.wrapper_fadein_window').hide();
            }
        );
        $(document).keydown
        (
            function(event)
            {
                if (event.keyCode === 27) {
                    $('.wrapper_fadein_window .close').trigger('click');
                }
            }
        );

        /* ----- Tables ----- */
        
        $('.table').on
        (
            'mouseenter mouseleave',
            '.show',
            function(event)
            {
                if (event.type === 'mouseenter') {
                    $(this).closest('tr').addClass('show_tr');
                } else {
                    $(this).closest('tr').removeClass('show_tr');
                }
            }
        );

        InputFileStyle($('input[type=file]'));
    }
);