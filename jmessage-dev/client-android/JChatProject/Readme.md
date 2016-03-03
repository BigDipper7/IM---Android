### Bug
    * bug fix in it,  
    we must make `ListView` height to be a fixed value, it means the height should be `match_parent` 
    also if this `ListView` has the parent node, the parent layout also should be `match_parent`, otherwise  
    the 'getView()' will be called for many times, because the height is not fixed, it will be redraw for  
    many times.
    