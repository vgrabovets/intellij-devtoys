package lermitage.intellij.ilovedevtoys.toolwindow.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.JSplitPane;

@State(
    name = "JSONStringTool",
    storages = @Storage("dev_toys.xml"),
    category = SettingsCategory.PLUGINS
)
public final class JSONStringToolSettings implements PersistentStateComponent<JSONStringToolSettings> {
    public int DIVIDER_LOCATION = 100;
    public int DIVIDER_ORIENTATION = JSplitPane.VERTICAL_SPLIT;

    @Override
    public JSONStringToolSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull JSONStringToolSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static JSONStringToolSettings getInstance() {
        return ApplicationManager.getApplication().getService(JSONStringToolSettings.class);
    }
}
