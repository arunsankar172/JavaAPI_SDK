class Main{
    public static void main(String[] args) {
        if(args.length>0) {
            System.out.println(args.length);
            APILogic apiLogic = new APILogic();
            switch (args[0]) {
                case "list":
                    apiLogic.listEmployee();
                    break;
                case "create":
                    if(args.length==6) {
                        apiLogic.createEmployee(args[1], args[2], args[3], args[4], args[5]);
                    }
                    break;

                case "update":
                    if(args.length==4) {
                        apiLogic.updateEmployee(args[1], args[2], args[3]);
                    }
                    break;
                case "delete":
                    if(args.length==2) {
                        apiLogic.deleteEmployee(args[1]);
                    }
                    break;
                case "upload":
                    if(args.length==3) {
                        apiLogic.addResume(args[1],args[2]);
                    }
                    break;
                case "download":
                    if(args.length==2) {
                        apiLogic.download(args[1]);
                    }
                    break;
                default:
                    System.out.println("Invalid Parameters");
            }
        }
    }

}
