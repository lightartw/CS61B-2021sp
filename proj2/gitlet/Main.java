package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {

        args = new String[]{"status"};
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
               validateNumArgs("add", args, 2);
               Repository.add(args[1]);
               break;
            case "commit":
                validateNumArgs("commit", args, 0);
                Repository.commit(args[1], null);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "checkout":
                validateNumArgs("checkout", args, 0);
                Repository.checkout(args);
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                validateNumArgs("reset", args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validateNumArgs("merge", args, 2);
                Repository.merge(args[1]);
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                break;
        }
    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (cmd.equals("commit")) {
            if (args.length != 2) {
                System.err.println("Please enter a commit message.");
                System.exit(0);
            }
        }
        else if (cmd.equals("checkout")) {
            if (args.length < 2) {
                System.err.println("Incorrect operands");
                System.exit(0);
            }
        }
        else if (args.length != n) {
            System.err.println("Incorrect operands");
            System.exit(0);
        }
    }
}
