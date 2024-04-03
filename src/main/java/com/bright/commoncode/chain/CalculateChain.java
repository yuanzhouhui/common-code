package com.bright.commoncode.chain;

/**
 * @author yuanzhouhui
 * @description
 * @date 2024-04-02 17:58
 */
public abstract class CalculateChain {

    protected CalculateChain next;

    public void setNext(CalculateChain next) {
        this.next = next;
    }

    public abstract void doCalculate();

    public static class Builder {
        private CalculateChain head;
        private CalculateChain tail;

        public Builder addChain(CalculateChain chain) {
            if (head == null) {
                this.head = this.tail = chain;
                return this;
            }
            this.tail.next = chain;
            this.tail = chain;
            return this;
        }

        public CalculateChain build() {
            return this.head;
        }
    }
}
