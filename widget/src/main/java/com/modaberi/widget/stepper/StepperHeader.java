package com.modaberi.widget.stepper;

public class StepperHeader {
    String title;
    int position;
    EnumStep step;

    public StepperHeader(String title, int position, EnumStep step) {
        this.title = title;
        this.position = position;
        this.step = step;
    }

    public enum EnumStep {
        CURRENT("CURRENT", 1),
        DONE("DONE", 2),
        NEXT("NEXT", 3);
        public String methodName;
        public int methodValue;

        EnumStep(String methodName, int methodValue) {
            this.methodName = methodName;
            this.methodValue = methodValue;
        }
    }
}