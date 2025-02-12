package cn.wp.hpc.module.promotion.enums.common;

import cn.wp.hpc.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 营销的条件类型枚举
 *
 *
 */
@AllArgsConstructor
@Getter
public enum PromotionConditionTypeEnum implements ArrayValuable<Integer> {

    PRICE(10, "满 N 元"),
    COUNT(20, "满 N 件");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(PromotionConditionTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 类型值
     */
    private final Integer type;
    /**
     * 类型名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
