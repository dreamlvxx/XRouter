package com.dream.annotation_complier.base;

import javax.lang.model.element.Element;



public interface OnRegulaListener<T extends Element> {
    /**
     * @param element
     * @return return null or "" to declare that the element is correct
     */
    String onRegula(T element);
}