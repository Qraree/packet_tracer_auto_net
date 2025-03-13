    package org.example;
    import com.cisco.pt.ptmp.PacketTracerSession;
    import com.cisco.pt.ptmp.PacketTracerSessionFactory;
    import com.cisco.pt.ptmp.impl.PacketTracerSessionFactoryImpl;
    import com.cisco.pt.impl.OptionsManager;
    import com.cisco.pt.ptmp.ConnectionNegotiationProperties;
    import com.cisco.pt.ipc.IPCFactory;
    import com.cisco.pt.ipc.ui.IPC;
    import com.cisco.pt.ipc.system.NetworkFile;
    import com.cisco.pt.ipc.system.Options;
    import com.cisco.pt.ipc.ui.AppWindow;
    import com.cisco.pt.ipc.enums.BufferFullAction;

    public class PacketTracerConnector {
        public static void main(String[] args) {
            try {
                // 1. Создаём фабрику сессий
                PacketTracerSessionFactory sessionFactory = PacketTracerSessionFactoryImpl.getInstance();

                // 2. Загружаем настройки соединения
                ConnectionNegotiationProperties cnp = OptionsManager.getInstance().getConnectOpts();

                // 3. Указываем параметры аутентификации
                cnp.setAuthenticationSecret("cisco"); // Пароль для подключения
                cnp.setAuthenticationApplication("com.example.myapp"); // ID приложения

                // 4. Подключаемся к Packet Tracer (порт 39000)
                PacketTracerSession session = sessionFactory.openSession("localhost", 39000, cnp);

                // 5. Получаем объект IPCFactory
                IPCFactory ipcFactory = new IPCFactory(session);
                IPC ipc = ipcFactory.getIPC();

                // 6. Доступ к GUI Packet Tracer
                AppWindow appWindow = ipc.appWindow();


                // 7. Переключаем PT в режим симуляции
                appWindow.getRSSwitch().showSimulationMode();

                // 8. Получаем текущий файл сети
                NetworkFile networkFile = appWindow.getActiveFile();

                // 9. Меняем настройки буфера
                Options options = networkFile.getOptions();
                options.setBufferFullAction(BufferFullAction.AUTO_CLEAR_EVENT_LIST);

                System.out.println("Подключение к Packet Tracer установлено!");
                System.out.println("Подключение к Packet Tracer установлено!");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка при подключении к Packet Tracer.");
            }
        }
    }
