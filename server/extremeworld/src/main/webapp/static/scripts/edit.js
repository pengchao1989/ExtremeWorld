/**
 * 
 */
(function() {
  $(function() {

    var editor, mobileToolbar, toolbar, allowedTags;
    toolbar = [ 'link', 'image'];
    allowedTags = ['a', 'img'];
    return editor = new Simditor({
      textarea: $('#txt-content'),
      placeholder: '这里输入文字...',
      toolbar: toolbar,
      pasteImage: true,
      upload: location.search === '?upload' ? {
          url: 'http://7u2nc3.com1.z0.glb.clouddn.com',
          
        } : true,
        allowedTags:allowedTags
       
      
    });
  });

}).call(this);