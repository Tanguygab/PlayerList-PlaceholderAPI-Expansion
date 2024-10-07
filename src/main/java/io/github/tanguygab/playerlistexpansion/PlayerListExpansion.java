package io.github.tanguygab.playerlistexpansion;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import io.github.tanguygab.playerlistexpansion.groups.GroupedList;
import io.github.tanguygab.playerlistexpansion.groups.ListGroup;
import io.github.tanguygab.playerlistexpansion.sorting.SortingType;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.Taskable;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PlayerListExpansion extends PlaceholderExpansion implements Taskable, Configurable {

	private static PlayerListExpansion instance;
	private final Map<String, PlayerList> lists = new HashMap<>();
	private final Map<String, ListGroup> listGroups = new HashMap<>();
	private final List<String> placeholders = new ArrayList<>();

	public String offlineText;
	public String argumentSeparator;
	private final static Map<String, Object> defaults = new LinkedHashMap<String,Object>() {{
		put("offline-text","Offline");
		put("argument-separator",",");
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
	public @NotNull String getAuthor() {
		return "Tanguygab";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "playerlist";
	}

	@Override
	public @NotNull String getVersion() {
		return "3.0.8";
	}

	@Override
	public @NotNull List<String> getPlaceholders() {
		return placeholders;
	}

	@Override
	public void start() {
		offlineText = getString("offline-text","Offline");
		argumentSeparator = Pattern.quote(getString("argument-separator", ","));

		ConfigurationSection config = getConfigSection("lists");
		if (config == null) return;
		config.getValues(false).forEach((list, obj) -> {
			ConfigurationSection cfg = getConfigSection("lists." + list);
			if (cfg == null) return;

			String type0 = cfg.getString("type", "ONLINE");
			ListType type = ListType.find(type0);
			if (type == null) {
				log(Level.CONFIG, "List \""+list+"\" has an invalid type \"" + type0 + "\"! Skipping...");
				return;
			}

			boolean included = cfg.getBoolean("included", true);
			boolean duplicates = cfg.getBoolean("duplicates", true);

			List<Filter> filters = new ArrayList<>();
			cfg.getStringList("filters").forEach(filter -> {
				Filter f = Filter.find(filter);
				if (f == null) {
					log(Level.CONFIG, "List \""+list+"\" has an invalid filter type \"" + filter + "\"! Skipping...");
					return;
				}
				filters.add(f);
			});

			List<SortingType> sortingTypes = new ArrayList<>();
			cfg.getStringList("sorting-types").forEach(sortingType -> {
				SortingType s = SortingType.find(sortingType);
				if (s == null) {
					log(Level.CONFIG, "List \""+list+"\" has an invalid sorting type \"" + sortingType + "\"! Skipping...");
					return;
				}
				sortingTypes.add(s);
			});

			lists.put(list, new PlayerList(list,type,filters,sortingTypes,included,duplicates));
		});
		placeholders.addAll(lists.keySet().stream().map(listName->"%playerlist_"+listName+"_<list|amount|#>%").collect(Collectors.toList()));


		ConfigurationSection groups = getConfigSection("groups");
		if (groups == null) return;
		groups.getValues(false).forEach((group, obj) -> {
			ConfigurationSection cfg = getConfigSection("groups." + group);
			if (cfg == null) return;

			int gap = Math.max(0, cfg.getInt("gap", 1));

			ConfigurationSection listsCfg = cfg.getConfigurationSection("lists");
			if (listsCfg == null || listsCfg.getValues(false).isEmpty()) {
				log(Level.CONFIG, "Group \""+group+"\" is empty! Skipping...");
				return;
			}
			List<GroupedList> lists = new ArrayList<>();
			listsCfg.getValues(false).forEach((list,settings0) -> {
				if (!this.lists.containsKey(list)) {
					log(Level.CONFIG, "List \""+list+"\" in group \""+group+"\" doesn't exist! Skipping...");
					return;
				}

				ConfigurationSection settings = (ConfigurationSection) settings0;

				String title = settings.getString("title", list+" (%amount%)");
				int max = Math.max(0, settings.getInt("max", 0));
				String remaining = settings.getString("remaining");

				lists.add(new GroupedList(this.lists.get(list), title, max, remaining));
			});

			if (lists.isEmpty()) {
				log(Level.CONFIG, "Group \""+group+"\" is empty! Skipping...");
				return;
			}
			listGroups.put(group, new ListGroup(lists, gap));
		});
		placeholders.addAll(listGroups.keySet().stream().map(groupName->"%playerlist_group_"+groupName+"_<amount|#>%").collect(Collectors.toList()));

	}

	@Override
	public void stop() {}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		int _index = identifier.lastIndexOf('_');
		if (_index == -1 || _index == identifier.length()-1) return null;
		String list = identifier.substring(0,_index);
		String output = identifier.substring(_index+1);

		if (list.startsWith("group_")) {
			String group = list.substring(6);
			return listGroups.containsKey(group) ? listGroups.get(group).getText(player,output) : null;
		}

		return lists.containsKey(list) ? lists.get(list).getText(player,output) : null;
	}

	@Override
	public Map<String, Object> getDefaults() {
		return defaults;
	}
}
