package agent.search.non_ai;

import java.util.List;
import java.util.Set;

import agent.Agent;
import game.model.GameBoard;
import game.model.Node;
import game.model.Pair;
import game.model.Player;
import game.model.info_capsules.Attack;
/**
 * 
 * @author Michael
 * Agent that always places all its bonus armies on the vertex with the most
 * armies, and greedily attempts to attack so as to cause the most damage
 * i.e: to prevent its opponent getting a continent bonus (the largest possible).
 */
public class AggressiveAgent implements Agent {

	/**
	 * player uses this agent.
	 */
	private Player player;
	/**
	 * node to place armies in.
	 */
	private Node placeNode;
	/**
	 * Attack action. 
	 */
	private Attack attack;
	
	public AggressiveAgent(Player player) {
		this.player = player;
	}
	
	@Override
	public String getAgentName() {
		return "AggressiveAgent";
	}

	@Override
	public Player getAgentPlayer() {
		return this.player;
	}

	@Override
	public void observe_enviroment(GameBoard board) {
		int maxArmies = 0;
		Node tempPlaceNode = null;
		int maxDamage = 0;
		Attack tempAttack = new Attack(false, 0, 0, 0);
		Set<Node> nodes = board.getPlayerNodesSet(player);
		for (Node node: nodes) {
			if (node.getArmies() > maxArmies ||
					(node.getArmies() == maxArmies && node.getId() < tempPlaceNode.getId())) {
				maxArmies = node.getArmies();
				tempPlaceNode = node;
			}
		}
		List<Pair<Integer, Integer>> attackingEdges = board.getAttackingEdges();
		for (int i = 0; i < attackingEdges.size(); i++) {
			Node plNode, opNode;
			if (board.node_belongs_to(player, attackingEdges.get(i).first)) {
				plNode = board.getNodeById(player, attackingEdges.get(i).first);
				opNode = board.getNodeById(player.reverseTurn(), attackingEdges.get(i).second);
			} else {
				plNode = board.getNodeById(player, attackingEdges.get(i).second);
				opNode = board.getNodeById(player.reverseTurn(), attackingEdges.get(i).first);
			}
			if (plNode == tempPlaceNode) {
				plNode.setArmies(plNode.getArmies() + board.get_turn_unit_number(player));
			}
			if (plNode.getArmies() - opNode.getArmies() > 1 && opNode.getArmies() > maxDamage || 
					(plNode.getArmies() - opNode.getArmies() > 1 && opNode.getArmies() == maxDamage && tempAttack.src == plNode.getId() )) {
				maxDamage = opNode.getArmies();
				// assume all possible armies will go to new node conquered.
				tempAttack = new Attack(true, plNode.getId(), opNode.getId(), plNode.getArmies() - opNode.getArmies() - 1);
			}
		}
		this.attack = tempAttack;
		this.placeNode = tempPlaceNode;
		
	}

	@Override
	public int place_action() {
		if (placeNode == null) {
			throw new RuntimeException("Must place armies in a node.");
		}
		return placeNode.getId();
	}

	@Override
	public Attack attack_action() {
		return this.attack;
	}

	

}
