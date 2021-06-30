package com.dream.xxrouter;

import com.dream.annotation_ann.annotion.Dann;
import com.dream.annotation_ann.annotion.Para;

public interface TestInterface {
    @Dann
    void getMessage(@Para String paraname);
}
