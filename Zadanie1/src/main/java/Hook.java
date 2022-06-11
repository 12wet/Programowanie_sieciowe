interface Hook {

    void stopConnection();

    default void addHook(){
        Runtime.getRuntime()
                .addShutdownHook(new Thread(this::stopConnection));
    }
}
