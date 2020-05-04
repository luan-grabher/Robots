package Robo;

import Auxiliar.Parametros;
import Auxiliar.Valor;
import Entity.Executavel;
import Executor.Execution;
import Executor.View.View;
import java.io.File;
import java.util.List;
import main.Arquivo;

public class AppRobo {

    private String nome;
    private File arquivoParametros = new File("//localhost/Robos/Tarefas/parametros.cfg").getAbsoluteFile();
    private File localRetorno = new File("//localhost/Robos/Retornos de Tarefas/").getAbsoluteFile();
    private Parametros parametros;
    private Execution execução;

    public AppRobo(String nome) {
        this.nome = nome;

        execução = new Execution("Executando robô " + this.nome, 1);
        execução.setMostrarMensagens(false);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void definirParametros() {
        try {
            parametros = new Parametros(arquivoParametros);
        } catch (Exception e) {
            parametros = new Parametros("");
        }
    }

    public void definirParametros(String textoParametros) {
        parametros = new Parametros(textoParametros);
    }

    public String getRetornoPadrao() {
        return "Parece que eu perdi a mensagem que eu deveria mostrar para você, "
                + "mas não se preocupe, geralmente isso acontece quando faço o meu "
                + "trabalho coretamente e não tenho nada para falar. Você poderia "
                + "conferir se eu fiz o que deveria ter feito? Caso eu não tenha "
                + "feito por favor contate o programador!";
    }

    public void executar(String retornoFuncao) {
        try {
            String titulo = "Retorno da tarefa " + nome;

            //Verifica se possui Id
            Valor idTarefa = parametros.get("idTarefa");
            if (idTarefa.getInteger() > 0) {
                retornoFuncao = retornoFuncao.equals("") ? getRetornoPadrao() : retornoFuncao;
                retornoFuncao = retornoFuncao.replaceAll("\n", "<br>");
                if (!Arquivo.salvar(localRetorno.getAbsolutePath() + "/" + idTarefa.getString() + ".html", titulo + "§" + retornoFuncao)) {
                    System.out.println("Ocorreu um erro ao salvar retorno da tarefa!");
                }
            } else {
                String[] options = new String[]{"PrintLn", "Arquivo no Desktop"};
                int option = View.chooseOption("Teste", "Você está testando o robô escolha como quer visualizar o retorno:", options);
                if (option == 0) {
                    System.out.println(titulo + "\n\n" + retornoFuncao.replaceAll("<br>", "\n"));
                } else {
                    File desktop = new File(System.getProperty("user.home") + "/Desktop");
                    Arquivo.salvar(desktop.getAbsolutePath() + "/test robo.html", titulo + "<br><br>" + retornoFuncao);
                }
            }
        } catch (Exception e) {
            System.out.println("Ocorreu a excessão: " + e);
            e.printStackTrace();
        } catch (Error e) {
            System.out.println("Ocorreu um erro: " + e);
            e.printStackTrace();
        } finally {
            //Finaliza JFRAME
            execução.finalizar();
        }

        //Garante fechamento Robô
        System.exit(0);
    }

    public Valor getParametro(String nomeParametro) {
        return parametros.get(nomeParametro);
    }

    public void setArquivoParametros(File arquivoParametros) {
        this.arquivoParametros = arquivoParametros;
    }

    public void setLocalRetorno(File localRetorno) {
        this.localRetorno = localRetorno;
    }

    public static String rodarExecutaveis(String nomeApp, List<Executavel> executaveis) {
        Execution execucao = new Execution(nomeApp);
        execucao.setExecutaveis(executaveis);
        execucao.setMostrarMensagens(false);
        execucao.rodarExecutaveis();
        execucao.finalizar();
        return execucao.getRetorno();
    }
}
