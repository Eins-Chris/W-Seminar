package v1;

public class CommandExecutor extends Thread {

    private ProjectView view;
    private String command;
    private String[] args;
    private int[] intargs;

    public CommandExecutor(String command, ProjectView view) {
        this.command = command;
        this.view = view;
    }

    @Override
    public void run() {
        view.output("[Input] - /" + command);
        if (!command.contains("(")) {
            switch (command) {
                case "start":
                    view.start();
                    break;
                case "stop":
                    view.stop();
                    break;
                case "reset":
                    view.init();
                    break;
                case "end":
                    view.dispose();
                    break;
                case "cls":
                    view.getOutputArea().setText("");
                    break;
                default:
                    view.output("[ERROR] - Unknown command '/" + command + "'");
                    break;
            }
        } else {
            if (!command.contains(")")) {
                view.output("[ERROR] - Wrong syntax! Missing ')'");
            } else {
                String tcmd = command.replaceAll(" ", "");
                String ckcmd = tcmd.replaceAll(",", "");
                if (ckcmd.substring(ckcmd.indexOf("("), ckcmd.indexOf(")")+1).equals("()")) {
                    view.output("[ERROR] - Empty '()'");
                } else {
                    args = tcmd.substring(tcmd.indexOf('(') + 1, tcmd.indexOf(')')).split("\\s*,\\s*");
                    intargs = new int[args.length];
                    switch (tcmd.substring(0, tcmd.indexOf("("))) {
                        case "setOrganism":
                            if (getArgsLength() != 2) {
                                view.output("[ERROR] - Wrong syntax! Use: /setOrganism(int, int)");
                            } else {
                                if (getiArgs(0) >= 0 && getiArgs(1) >= 0 && getiArgs(0) < view.getVariable().GRIDSIZE && getiArgs(1) < view.getVariable().GRIDSIZE) {
                                    view.organism_list.add(new Organism(view.getVariable(), getiArgs(0), getiArgs(1)));
                                } else view.output("[ERROR] - IndexOutOfBoundsException: Input (" + getiArgs(0) + "," + getiArgs(1) + ")   < 0 or >= " + view.getVariable().GRIDSIZE);
                            }
                            break;
                        default:
                            view.output("[ERROR] - Unknown command '/" + command + "'");
                            break;
                    }
                }
            }
        }
        view.print();
    }

    private void initintargs() {
        for (int i = 0; i < args.length; i++) {
            try {
                intargs[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                intargs[i] = 0;
                view.output("[ERROR] - Ungültige Zahl: " + args[i]);
            }
        }
    }

    private int getArgsLength() {
        initintargs();
        return args.length;
    }

    private String getSArgs(int index) {
        return args[index];
    }

    private int getiArgs(int index) {
        initintargs();
        return intargs[index];
    }
}
