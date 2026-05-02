public class Bus extends Vehicle {
    private Terminal terminalStart;
    private Terminal terminalEnd;
    private int completedRounds = 0;

    public void setTerminalStart(Terminal t) { this.terminalStart = t; }
    public void setTerminalEnd(Terminal t)   { this.terminalEnd = t; }
    public Terminal getTerminalStart() { return terminalStart; }
    public Terminal getTerminalEnd()   { return terminalEnd; }

    @Override
    public void step(boolean random) {
        // Bus movement is driven by Busz_Lep command directly, no autonomous step
    }

    public void arrivedAtTerminal() {
        completedRounds++;
        Terminal tmp = terminalStart;
        terminalStart = terminalEnd;
        terminalEnd = tmp;
        Logger.action(this, "Megerkezett vegallomashoz, fordulok: " + completedRounds);
    }

    public int getCompletedRounds() { return completedRounds; }
    public boolean isRouteComplete(int expected) { return completedRounds >= expected; }
}
