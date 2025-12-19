package pl.edu.wszib.librarymanager.core;

import org.springframework.stereotype.Component;
import pl.edu.wszib.librarymanager.gui.IGUI;

@Component
public class Core implements ICore {

    private final IGUI gui;

    public Core(IGUI gui) {
        this.gui = gui;
    }

    @Override
    public void run() {
        gui.start();
    }
}
