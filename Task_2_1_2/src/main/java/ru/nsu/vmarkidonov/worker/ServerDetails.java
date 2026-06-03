package ru.nsu.vmarkidonov.worker;

/**
 * Данные для подключения к обнаруженному серверу.
 *
 * @param ipAddress IP-адрес сервера в локальной сети
 * @param port TCP-порт сервера для обмена данными
 */
public record ServerDetails(String ipAddress, int port) {

}
