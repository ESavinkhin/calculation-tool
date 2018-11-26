package ru.sberbank.tool;

import ru.sberbank.tool.services.MergeServiceImpl;

import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        if (args.length < 3) {
            throw CalculatorErrors.CALCULATOR_ERROR.error("Must have 2 arguments client order output");
        }

        try {
            File output = new File(args[2]);
            if (output.createNewFile()) {
                throw CalculatorErrors.CALCULATOR_ERROR.error("Cant create file", output.getAbsolutePath());
            }

            new MergeServiceImpl().calculateClient(
                    new File(args[0]),
                    new File(args[1]),
                    new File(args[2]));
        } catch (IOException e) {
            throw CalculatorErrors.CALCULATOR_ERROR.error(e.getMessage(), e.toString());
        }
    }
}
