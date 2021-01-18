package main.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode extends RuntimeException {
    // 1xx: exception and cannot be handled by software
    public static final int IO_EXCEPTION = 101;
    public static final int CLASS_NOT_FOUND_EXCEPTION = 102;
    public static final int FILE_NOT_FOUND_EXCEPTION = 103;
    public static final int MD5_INIT_FAILED = 104;

    // 2xx: file error and can be handled by software
    public static final int CHECKSUM_CHECK_FAILED = 201;

    // 3xx: user unexpected input and can be handled by software
    public static final int FILE_NOT_FOUND = 301;
    public static final int FILE_ALREADY_EXIST = 302;
    public static final int INVALID_ARGUMENT = 303;
    public static final int END_OF_FILE = 304;
    public static final int INVALID_CURSOR_MOVE = 305;
    public static final int BLOCK_NOT_FOUND = 306;
    public static final int MANAGER_NOT_EXIST = 307;
    public static final int NOT_OPERATING_FILE_NOW = 308;

    // 10xx: system error
    public static final int SYSTEM_ERROR = 1000;
    public static final int UNKNOWN = 1001;

    private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();

    static {
        ErrorCodeMap.put(IO_EXCEPTION, "IO Exception");
        ErrorCodeMap.put(CLASS_NOT_FOUND_EXCEPTION, "Class Not Found Exception");
        ErrorCodeMap.put(FILE_NOT_FOUND_EXCEPTION, "File Not Found Exception");
        ErrorCodeMap.put(MD5_INIT_FAILED, "MD5 Initialization Failed");
        ErrorCodeMap.put(CHECKSUM_CHECK_FAILED, "Block Checksum Check Failed");
        ErrorCodeMap.put(FILE_NOT_FOUND, "File Does Not Exist");
        ErrorCodeMap.put(FILE_ALREADY_EXIST, "File Already Exists");
        ErrorCodeMap.put(INVALID_ARGUMENT, "Invalid Argument Input");
        ErrorCodeMap.put(END_OF_FILE, "End Of File");
        ErrorCodeMap.put(INVALID_CURSOR_MOVE, "Invalid Cursor Move Operation");
        ErrorCodeMap.put(BLOCK_NOT_FOUND, "Block Does Not Exist");
        ErrorCodeMap.put(MANAGER_NOT_EXIST, "File Manager Or Block Manager Out Of Size");
        ErrorCodeMap.put(SYSTEM_ERROR, "System Error");
        ErrorCodeMap.put(UNKNOWN, "Unknown");
    }

    private final int errorCode;

    public ErrorCode(int errorCode) {
        super(String.format("error code '%d' \"%s\"", errorCode, getErrorText(errorCode)));
        this.errorCode = errorCode;
    }

    public static String getErrorText(int errorCode) {
        return ErrorCodeMap.getOrDefault(errorCode, "invalid");
    }

    public static void ErrorCodeHandler(int errorCode) {
        switch (errorCode) {
            case CHECKSUM_CHECK_FAILED:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("This file has been damaged, and the file system failed to read its data.");
                break;
            case FILE_NOT_FOUND:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("This file doesn't exist now. Input correct file name or create this file.");
                break;
            case FILE_ALREADY_EXIST:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("This file already exists. Get this file or create a new file with another file name.");
                break;
            case INVALID_ARGUMENT:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("Argument [length] should ≥ 0. Negative length is confusing and it's not accepted.");
                break;
            case END_OF_FILE:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("This read operation reaches the end of the file. Fail to read file data.");
                break;
            case INVALID_CURSOR_MOVE:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("The cursor is either over file size or bellow zero, or [where] argument should be 0, 1 or 2. Fail to move cursor.");
                break;
            case BLOCK_NOT_FOUND:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("This block doesn't exist now. Either this file system or this file is damaged.");
                break;
            case MANAGER_NOT_EXIST:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("This file/block manager doesn't exist now. The input is larger than your total file/block manager number.");
                break;
            case NOT_OPERATING_FILE_NOW:
                System.out.printf("error code: [%d] [%s]%n", errorCode, getErrorText(errorCode));
                System.out.println("You aren't operating a file now. Please create or open a file first.");
                break;
            default:
                System.out.println("This operation is invalid. It's not accepted.");
                break;
        }
        System.out.println("Please reenter your instruction.");
    }

    public int getErrorCode() {
        return errorCode;
    }
}
