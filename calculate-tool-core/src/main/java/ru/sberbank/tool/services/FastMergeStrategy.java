package ru.sberbank.tool.services;

import ru.sberbank.tool.CalculatorErrors;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Order;

import java.util.stream.Stream;

/**
 * Created by Egor Savinkhin on 22/11/2018.
 * Property of Sberbank
 * <p>
 * Реалзиация быстрого алгоритма слияния
 */
class FastMergeStrategy implements MergeStrategy {

    @Override
    public Stream<Client> merge(Stream<Client> clientStream, Stream<Order> orderStream) {
        /*
          1. сортируем списки
          2. за один проход в цикле соединяем элементы
          сложность N
         */
        /*List<Client> list1 = clientStream.collect(Collectors.toList());
        List<Order> list2 = orderStream.collect(Collectors.toList())
        List<Client> allList = new ArrayList<>();
        int l1 = 0, l2 = 0;
        for (int i = 0; i < list1.size() + list2.size(); i++)
            if (l1 < list1.size() && l2 < list2.size())
                if (list1.get(l1).getAge() > list2.get(l2).getAge())
                    allList.add(list1.get(l1++));
                else if (list1.get(l1).getAge() < list2.get(l2).getAge())
                    allList.add(list2.get(l2++));
                else if (allList.size() > 0) {
                    if (list1.get(l1).getName().contains("L1") == allList.get(allList.size() - 1).getName().contains("L1"))
                        allList.add(list2.get(l2++));
                    else
                        allList.add(list1.get(l1++));
                } else
                    allList.add(list1.get(l1++));
            else if (list1.size() == l1)
                allList.add(list2.get(l2++));
            else
                allList.add(list1.get(l1++));*/
        throw CalculatorErrors.NOT_IMPLEMENT.error();
    }

    @Override
    public boolean isApplicable(long sizeClient, long sizeProduct) {
        return sizeClient > 20;
    }

}
