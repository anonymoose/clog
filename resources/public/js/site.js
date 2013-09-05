

/* KB: [2013-09-03]: Update workspace and markdown based on which side you are editing. */
$(document).ready(function() {
    // Enable Hallo editor
    $('.editable').hallo({
        plugins: {
            'halloformat': {},
            'halloheadings': {},
            'hallolists': {},
            'halloreundo': {}
        },
        toolbar: 'halloToolbarFixed'
    });
    
    var markdownize = function(workspace) {
        var html = workspace.split("\n").map($.trim).filter(function(line) { 
            return line != "";
        }).join("\n");
        return toMarkdown(html);
    };
    
    var converter = new Showdown.converter();
    var htmlize = function(workspace) {
        return converter.makeHtml(workspace.trim());
    };
    
    var showSource = function(workspace) {
        var markdown = markdownize(workspace);
        if ($('#workspace').get(0).value == markdown) {
            return;
        }
        $('#workspace').get(0).value = markdown;
    };
    
    var updateHtml = function(workspace) {
        if (markdownize($('.editable').html()) == workspace) {
            return;
        }
        var html = htmlize(workspace);
        $('.editable').html(html); 
        $('#content').html(html); 
    };
    
    $('.editable').bind('hallomodified', function(event, data) {
        showSource(data.workspace);
    });
    $('#workspace').bind('keyup', function() {
        updateHtml(this.value);
    });
    showSource($('.editable').html());
    //updateHtml($('#workspace').val());

    $('#view_select').bind('change', function() {
        $('.toggle-view').toggle();
    });
}); 



