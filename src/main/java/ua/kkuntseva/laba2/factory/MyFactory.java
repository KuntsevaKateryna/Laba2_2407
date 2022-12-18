package ua.kkuntseva.laba2.factory;

import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyFactory {
    Logger logger = LoggerFactory.getLogger(MyFactory.class);

    public FormatGenerator create_format(String file_format) {
        FormatGenerator fk = null;
        switch (file_format) {
            case "doc": fk = new MSWordFormat();
        }
        return fk;
    }



}
