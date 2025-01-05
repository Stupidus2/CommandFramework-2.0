package de.stupidus.tabCompleter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TabCompleterSuper {

    public List<Integer> generateArgLengthList(String subCommandName) {
        String[] subCommandArray = subCommandName.split(" ");
        return IntStream.range(0, subCommandArray.length)
                .filter(i -> subCommandArray[i].startsWith("<[") && subCommandArray[i].endsWith("]>"))
                .boxed()
                .collect(Collectors.toList());
    }
}
