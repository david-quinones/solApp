package com.solapp.common;

public class ReturnValues extends JsonData{

    private int returnCode;


    /**
     * Default constructor
     */
    public ReturnValues() {
        super();
    }

    /**
     * Constructor of a ReturnValue that represents a response with the specified return code without more data.These can be filled later.
     * @param returnCode return code
     */
    public ReturnValues(int returnCode) {
        super();
        this.returnCode = returnCode;
    }

    /**
     * Gets the response's return code.
     * @return return code
     */
    public int getReturnCode() {
        return returnCode;
    }

}
