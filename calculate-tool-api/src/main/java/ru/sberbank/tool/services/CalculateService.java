package ru.sberbank.tool.services;

import java.io.File;

/**
 * Created by Egor Savinkhin on 22/11/2018.
 * Property of Sberbank
 * <p>
 *
 * Интерфейс сервиса вычесления заказаов для клиентов
 */
public interface CalculateService {
    void calculateClient(File clientFile, File orderFile, File outputFile);
}
