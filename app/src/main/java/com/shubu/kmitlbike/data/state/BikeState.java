package com.shubu.kmitlbike.data.state;

public enum BikeState {
    BORROW_READY("Your bike is ready"), BORROW_SCAN_START("Connecting to the bike"), BORROW_SCAN_FINISH("Connected"), BORROW_START("Borrowing"), BORROW_COMPLETE("Completed"), //borrow state
    RETURN_READY("Ready to return"), RETURN_SCAN_START("Connecting to the bike"), RETURN_SCAN_FINISH("Connected"), RETURN_START("Returning"), RETURN_COMPLETE("Completed"); //return state

    private final String state;

    BikeState(final String state){
        this.state = state;
    }

    @Override
    public String toString(){
        return this.state;
    }
}
