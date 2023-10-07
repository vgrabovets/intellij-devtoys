package lermitage.intellij.ilovedevtoys.toolwindow.options;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lermitage.intellij.ilovedevtoys.toolwindow.ComboBoxWithImageItem;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComboBox;
import java.util.ArrayList;

@State(
    name = "DevToysToolWindowOptions",
    storages = @Storage("dev_toys.xml"),
    category = SettingsCategory.PLUGINS
)
public final class DevToysToolWindowOptions implements PersistentStateComponent<DevToysToolWindowOptions> {
    public ArrayList<String> ITEMS_ORDER = new ArrayList<>();

    @Override
    public DevToysToolWindowOptions getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DevToysToolWindowOptions state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static DevToysToolWindowOptions getInstance() {
        return ApplicationManager.getApplication().getService(DevToysToolWindowOptions.class);
    }

    public void saveItemsOrder(JComboBox<ComboBoxWithImageItem> toolComboBox) {
        ITEMS_ORDER = new ArrayList<>();
        for (int i = 0; i < toolComboBox.getItemCount(); i++) {
            ITEMS_ORDER.add(toolComboBox.getItemAt(i).title());
        }
    }
}
