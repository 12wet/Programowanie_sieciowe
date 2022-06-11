package com.exercise3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BandSearcher {
    private final static String API_URL = "https://api.discogs.com/artists/";
    private final static Map<Integer, String> bandIds;

    static{
        bandIds = new HashMap<>();
    }

    public static String getApiUrl(){
        return API_URL;
    }

    public static void search(Band band){
        compute(membersOfBands(retrieveMembers(band)));
    }

    private static List<Member> retrieveMembers(Band band){
        return band
                .getMembers()
                .stream()
                .map(dtoMember -> {
                    return MemberController.retrieveMember(dtoMember);
                })
                .collect(Collectors.toList());
    }

    private static Map<Integer, List<Member>> membersOfBands(List<Member> members){
        Map<Integer, List<Member>> output = new HashMap<>();
        members.forEach(member -> {
                    for(Band group : member.getGroups())
                        if(output.putIfAbsent(group.getId(),
                                new ArrayList<>(List.of(member))) != null) {
                            output.get(group.getId()).add(member);
                        }
                    else bandIds.put(group.getId(), group.getName());
                });
        return output;
    }

    private static void compute(Map<Integer, List<Member>> membersOfBands){
        boolean firstIteration = true;
        for(int bandID : membersOfBands.keySet()){
            if(bandID != Exercise3Application.DESIRED_ID &&
                    membersOfBands.get(bandID).size() > 1){
                if(!firstIteration)
                    System.out.print(", ");
                else
                    firstIteration = false;
                System.out.print(bandIds.get(bandID) + " (" + bandID + ")");
                for(Member member : membersOfBands.get(bandID))
                    System.out.print(
                            " " + member.getName() +
                            " (" + member.getId() + ")");

            }
        }
    }

}
