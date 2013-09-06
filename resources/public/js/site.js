

/* KB: [2013-09-03]: Update workspace and markdown based on which side you are editing. */
$(document).ready(function() {
    // http://ace.c9.io/#nav=about
    var editor = '';

    var init = function() {
        editor = ace.edit("workspace");
        editor.setTheme("ace/theme/idle_fingers");
        editor.getSession().setMode("ace/mode/markdown");
        editor.setHighlightActiveLine(false);
        editor.setKeyboardHandler("ace/keyboard/emacs");
        editor.renderer.setShowPrintMargin(false);

        var markdown = $('#content').val();
        editor.setValue(markdown);
        updateHtml(markdown);
    };

    var converter = new Showdown.converter();
    var htmlize = function(workspace) {
        return converter.makeHtml(workspace.trim());
    };
    
    var updateHtml = function(workspace_contents) {
        $('#content').val(workspace_contents);
        var html = htmlize(workspace_contents);
        $('#output').html(html);
    };

    $('#workspace').bind('keyup', function() {
        updateHtml(editor.getValue());
    });
    if ($("#output").length > 0) {
        init();
    }

    $('#view_select').bind('change', function() {
        $('.toggle-view').toggle();
    });
}); 







