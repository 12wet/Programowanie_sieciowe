package com.exercise4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupSearcher {
    private final static String API_URL = "https://api.discogs.com/artists/";
    private final static Map<Integer, String> groupIds;

    static{
        groupIds = new HashMap<>();
    }

    public static String getApiUrl(){
        return API_URL;
    }

    public static void search(Group group){
        compute(membersOfBands(retrieveMembers(group)));
    }

    private static List<Member> retrieveMembers(Group group){
        return group
                .getMembers()
                .stream()
                .map(MemberController::retrieveMember)
                .collect(Collectors.toList());
    }

    private static Map<Integer, List<Member>> membersOfBands(List<Member> members){
        Map<Integer, List<Member>> output = new HashMap<>();
        members.forEach(member -> {
                    for(Group group : member.getGroups()) {
                        if (output.putIfAbsent(group.getId(),
                                new ArrayList<>(List.of(member))) != null) {
                            output.get(group.getId()).add(member);
                        } else groupIds.put(group.getId(), group.getName());
                    }
                });
        return output;
    }

    private static void compute(Map<Integer, List<Member>> membersOfBands){
        final boolean[] firstIteration = {true};
        membersOfBands
                .keySet()
                .stream()
                .filter(groupID ->
                        (groupID != Exercise4Application.DESIRED_ID) &&
                        (membersOfBands.get(groupID).size() > 1)
                )
                .forEach(groupID -> {
                    if(!firstIteration[0])
                        System.out.print(", ");
                    else
                        firstIteration[0] = false;
                    System.out.print(groupIds.get(groupID) + " (" + groupID + ")");
                    membersOfBands.get(groupID)
                            .forEach(member -> System.out.print(
                                    " " + member.getName() +
                                    " (" + member.getId() + ")"));
                });

    }

}
