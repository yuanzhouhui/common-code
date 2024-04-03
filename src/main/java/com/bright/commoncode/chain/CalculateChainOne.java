package com.bright.commoncode.chain;

/**
 * @author yuanzhouhui
 * @description
 * @date 2024-04-03 9:47
 */
public class CalculateChainOne extends CalculateChain{

    @Override
    public void doCalculate() {
        System.out.println("CalculateChainOne");
        next.doCalculate();
    }
}
