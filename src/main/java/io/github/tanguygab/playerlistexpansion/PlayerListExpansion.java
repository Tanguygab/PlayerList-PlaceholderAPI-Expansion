package io.github.tanguygab.playerlistexpansion;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
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

	private static final Pattern FORMAT_PATTERN = Pattern.compile("_format:");

	private static PlayerListExpansion instance;
	private final Map<String, PlayerList> lists = new HashMap<>();
	private final Map<String, ListGroup> listGroups = new HashMap<>();
	private final List<String> placeholders = new ArrayList<>();

	public int updateCooldown;
	public String offlineText;
	public String argumentSeparator;
	private final static Map<String, Object> defaults = new LinkedHashMap<String,Object>() {{
		put("update-cooldown", 1000);
		put("offline-text", "Offline");
		put("argument-separator", ",");
		put("lists",new HashMap<String, Map<String,Object>>() {{
			Map<String,Object> staffList = new LinkedHashMap<String, Object>() {{
				put("type","ONLINE");
				put("included",true);
				put("filters",Arrays.asList("PERMISSION:group.staff","CANSEE"));
			}};
			put("staff",staffList);
			Map<String,Object> playersList = new LinkedHashMap<String, Object>() {{
				put("type","ONLINE");
				put("included",true);
				put("filters",Arrays.asList("!PERMISSION:group.staff","CANSEE"));
			}};
			put("players",playersList);
		}});
		put("groups", new HashMap<String, Map<String,Object>>() {{
			Map<String,Object> playerlistGroup = new LinkedHashMap<String, Object>() {{
				put("gap",1);
				Map<String,Object> lists = new LinkedHashMap<String, Object>() {{
					Map<String,Object> staffList = new LinkedHashMap<String, Object>() {{
						put("title","Staff Members (%amount%)");
						put("max",10);
						put("remaining","... and %remaining% more!");
					}};
					put("staff",staffList);
					Map<String,Object> playersList = new LinkedHashMap<String, Object>() {{
						put("title","Players (%amount%)");
					}};
					put("players",playersList);
				}};
				put("lists", lists);
			}};
			put("staffAndPlayers",playerlistGroup);
		}});
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
		return "3.0.9";
	}

	@Override
	public @NotNull List<String> getPlaceholders() {
		return placeholders;
	}

	@Override
	public void start() {
		updateCooldown = getInt("update-cooldown",1000);
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
	public String onRequest(OfflinePlayer player, @NotNull String params) {
		Matcher matcher = FORMAT_PATTERN.matcher(params);
		String _params = matcher.find() ? params.substring(0, matcher.start()) : params;

		int _index = _params.lastIndexOf('_');
		if (_index == -1 || _index == params.length()-1) return null;

		String list = params.substring(0,_index);
		String output = params.substring(_index+1);

		if (list.startsWith("group_")) {
			String group = list.substring(6);
			ListGroup groupedList = listGroups.get(group);
			return groupedList != null ? groupedList.getText(player,output) : null;
		}

		PlayerList playerList = lists.get(list);
		return playerList != null ? playerList.getText(player,output) : null;
	}

	@Override
	public Map<String, Object> getDefaults() {
		return defaults;
	}

	public String getFormat(String arg, String[] args) {
		if (args.length < 2) return "{player_name}";
		String argument = arg.substring(args[0].length()+1);
		return argument.startsWith("format:") ? argument.substring(7) : "{player_name}";
	}

}
