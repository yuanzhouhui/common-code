package com.bright.commoncode.chain;

/**
 * @author yuanzhouhui
 * @description
 * @date 2024-04-03 9:48
 */
public class ChainStart {
    public static void main(String[] args) {
        CalculateChain.Builder builder = new CalculateChain.Builder();
        builder.addChain(new CalculateChainOne())
                .addChain(new CalculateChainTwo());
        builder.build().doCalculate();
    }
}
