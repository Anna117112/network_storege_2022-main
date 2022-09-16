package com.geekbrains.cloud.netty;

import com.geekbrains.cloud.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Timeout;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {
    private String nik;
    private final String SERVERFILES=  "server_files";
    private Path serverPath;
    private Path currentDir;
    private DbAuthenticationProvider dbAuthenticationProvider;

    
  //  private BdConnect dbConnection;

    public CloudFileHandler() {
       this.dbAuthenticationProvider = new DbAuthenticationProvider();
       dbAuthenticationProvider.init();

        currentDir = Path.of(SERVERFILES);
        serverPath = Path.of(SERVERFILES);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //currentDir = Path.of(SERVERFILES).resolve(nik);
        //ctx.writeAndFlush(new ListFiles(serverPath));// отправляем  спиок файлов при подключении
        //System.out.println(serverPath + "channelActive");
        //System.out.println( nik + "channelActive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        // запрос на вход передаем логин и пароль
        if (cloudMessage instanceof LogPass logPass) {
            boolean auth = false;
            // ищем по базе данных если находим передаем true  если клиент есть
             auth = dbAuthenticationProvider.booleanNicknameByLoginAndPassword(logPass.getLogin(), logPass.getPassword());
             nik = dbAuthenticationProvider.getNicknameByLoginAndPassword(logPass.getLogin(), logPass.getPassword());
            System.out.println("Auth" + nik);
           // currentDir = Path.of(SERVERFILES);
            System.out.println(currentDir + "  logPass");
            System.out.println(auth);
            //currentDir = Path.of(SERVERFILES).resolve(nik);
           // if (Files.exists(currentDir)) {
                //currentDir = (currentDir.resolve(nik));
            if (auth){

                ctx.writeAndFlush(new Auth(true));
            } else
                //Files.createDirectory(currentDir);
          //  currentDir = currentDir.resolve(nik);
            ctx.writeAndFlush(new Auth(false));
        }

           // получаем от от панели авторизации ник
        else if (cloudMessage instanceof AuthToServer authToServer){
           // currentDir = Path.of(SERVERFILES).resolve(authToServer.getNik());

            nik = authToServer.getNik();

            System.out.println(currentDir + "  AuthToServer");
            // путь ло папки клиерта
            currentDir = Path.of(SERVERFILES).resolve(nik);
            // если такая папка есть то передаем в лист ее
            if (Files.exists(currentDir)) {
               // currentDir = (currentDir.resolve(nik));

                // отправляем на клиента
                    ctx.writeAndFlush(new ListFiles(currentDir));
                } else {
                // если нет со создаем
                Files.createDirectory(currentDir);
                //  currentDir = currentDir.resolve(nik);
                ctx.writeAndFlush(new ListFiles(currentDir));
            }
            }





       else if (cloudMessage instanceof FileRequest fileRequest) {
            ctx.writeAndFlush(new FileMessage(currentDir.resolve(fileRequest.getName())));// передаем путь до дирректории

        } else if (cloudMessage instanceof FileMessage fileMessage) {
            Files.write(currentDir.resolve(fileMessage.getName()), fileMessage.getData());
            ctx.writeAndFlush(new ListFiles(currentDir));
        }
        else if (cloudMessage instanceof PathUpRequest pathUpRequest) {
            // новй путь до родительсой дирректории
            Path path = Path.of(String.valueOf(currentDir.getParent()));
            // currentDir = currentDir.getParent();
            // если он совпадает с путем до корневой папки сервера где храняться папки всех клиентов то отправляем файли только клиента
            if (path.startsWith(serverPath)) {
                ctx.writeAndFlush(new ListFiles(currentDir));
// иначе перехрдим в родительскую дирркторию
            } else {
                currentDir = currentDir.getParent();
                ctx.writeAndFlush(new ListFiles(currentDir));
            }
        }

        // переход в поддиректорию
        else if (cloudMessage instanceof PathInRequest pathInRequest){
                 currentDir = (currentDir.resolve(pathInRequest.getName()));// прописываем новый путь до файла
                ctx.writeAndFlush(new ListFiles(currentDir));// передаем
            }
        }

    }

