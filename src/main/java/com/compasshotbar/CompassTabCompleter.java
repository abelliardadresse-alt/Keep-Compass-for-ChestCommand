package com.compasshotbar;

import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CompassTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender,
                                     org.bukkit.command.Command command,
                                     String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("compasshotbar.command")) {
                completions.add("reload");
                completions.add("give");
                completions.add("toggle");
                completions.add("status");
            }
        }

        return completions;
    }
}