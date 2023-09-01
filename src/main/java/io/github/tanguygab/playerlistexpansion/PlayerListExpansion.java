package io.github.tanguygab.playerlistexpansion;

import java.util.*;
import java.util.stream.Collectors;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.Taskable;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PlayerListExpansion extends PlaceholderExpansion implements Taskable, Configurable {

	private static PlayerListExpansion instance;
	private final Map<String, PlayerList> lists = new HashMap<>();
	private final List<String> placeholders = new ArrayList<>();

	public String offlineText;
	public String argumentSeparator;
	private final static Map<String, Object> defaults = new LinkedHashMap<String,Object>() {{
		put("offline-text","Offline");
		put("argument-separator","||");
		Map<String,Object> staffList = new LinkedHashMap<String, Object>() {{
			put("type","ONLINE");
			put("included",true);
			put("filters",Arrays.asList("PERMISSION:group.staff","CANSEE"));
		}};
		put("lists",new HashMap<String, Map<String,Object>>() {{put("staff",staffList);}});
	}};

	public PlayerListExpansion() {
		instance = this;
	}
	public static PlayerListExpansion get() {
		return instance;
	}

	@Override
	public void start() {
		offlineText = getString("offline-text","Offline");
		argumentSeparator = getString("argument-separator","||");

		ConfigurationSection config = getConfigSection("lists");
		if (config == null) return;
		config.getValues(false).forEach((list, obj) -> {
			ConfigurationSection cfg = getConfigSection("lists." + list);
			if (cfg == null) return;
			ListType type = ListType.find(cfg.getString("type", "ONLINE"));
			if (type == null) return;

			boolean included = cfg.getBoolean("included", true);

			List<Filter> filters = new ArrayList<>();
			cfg.getStringList("filters").forEach(filter -> {
				String[] args = filter.split(":");
				Filter f = Filter.find(args[0], args.length > 1 ? args[1] : null);
				if (f != null) filters.add(f);
			});

			lists.put(list, new PlayerList(list,type,filters,included));
		});
		placeholders.addAll(lists.keySet().stream().map(listName->"%playerlist_"+listName+"_<list|amount|#>%").collect(Collectors.toList()));
	}

	@Override
	public void stop() {}

	@Override
	public @NotNull String getAuthor() {
		return "Tanguygab";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "playerlist";
	}

	@Override
	public @NotNull String getVersion() {
		return "3.0.4";
	}

	@Override
	public @NotNull List<String> getPlaceholders() {
		return placeholders;
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		int _index = identifier.lastIndexOf('_');
		if (_index == -1 || _index == identifier.length()-1) return null;
		String list = identifier.substring(0,_index);
		String output = identifier.substring(_index+1);
		return lists.containsKey(list) ? lists.get(list).getText(player,output) : null;
	}

	@Override
	public Map<String, Object> getDefaults() {
		return defaults;
	}
}
