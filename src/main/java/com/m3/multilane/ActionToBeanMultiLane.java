package com.m3.multilane;

import com.m3.multilane.action.Action;
import org.apache.commons.beanutils.BeanUtils;

/**
 * A MultiLane which handles {@link com.m3.multilane.action.Action}
 */
public class ActionToBeanMultiLane extends MultiLaneTemplate<Action, Object> {

    /**
     * Returns aggregated values as a Java bean.
     *
     * @param clazz  bean class
     * @param <BEAN> bean type
     * @return result as a Java bean
     * @throws Exception failed to create instance
     */
    public <BEAN> BEAN collectValuesAsBean(Class<BEAN> clazz) throws Exception {
        BEAN bean = clazz.newInstance();
        BeanUtils.populate(bean, collectValues());
        return bean;
    }

}
