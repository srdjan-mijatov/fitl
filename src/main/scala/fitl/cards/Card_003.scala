
//  _____ _            _         _   _            _          _
// |  ___(_)_ __ ___  (_)_ __   | |_| |__   ___  | |    __ _| | _____
// | |_  | | '__/ _ \ | | '_ \  | __| '_ \ / _ \ | |   / _` | |/ / _ \
// |  _| | | | |  __/ | | | | | | |_| | | |  __/ | |__| (_| |   <  __/
// |_|   |_|_|  \___| |_|_| |_|  \__|_| |_|\___| |_____\__,_|_|\_\___|
//
//
// An scala implementation of the solo Tru'ng bots for the game
// Fire in the Lake, designed by Mark Herman and Volko Ruhnke
// published by GMT Games.
//
// Copyright (c) 2021 Curt Sellmer
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


package fitl.cards

import scala.collection.immutable.ListMap
import fitl.FireInTheLake._
import fitl.EventHelpers._
import fitl.Bot
import fitl.Bot.{ US_Bot, ARVN_Bot, NVA_Bot, VC_Bot }
import fitl.Human

// Unshaded Text
// Haiphong mined: NVA Resources –9. Linebacker II allowed when
// Support+Available >25 (mark).
//
// Shaded Text
// Bombing halt: NVA Resources +9. If Trail 0-2, Improve to 3.
//
// Tips
// For the unshaded effect, place the “Peace Talks” marker on the “Linebacker II” 
// US Pivotal Event card as a reminder of its loosened precondition.

object Card_003 extends EventCard(3, "Peace Talks",
  DualEvent,
  List(US, NVA, ARVN, VC),
  ListMap(US   -> (Critical    -> Unshaded),
          ARVN -> (Ignored -> Unshaded),
          NVA  -> (Performed   -> Shaded),
          VC   -> (Ignored -> Shaded))) {

  def unshadedEffective(faction: Faction): Boolean = game.pivotCardsAvailable(US)

  def executeUnshaded(faction: Faction): Unit = {
    decreaseResources(NVA, 9)
    if (game.pivotCardsAvailable(US)) {
      log("\nPlace the \"Peace Talks\" marker on the Linebacker II pivotal event card", Color.Event)
      game = game.copy(peaceTalks = true)
    }
  }

  // Bot does not play to increment resources since they are not used.
  def shadedEffective(faction: Faction): Boolean = game.trail <= 2


  def executeShaded(faction: Faction): Unit = {
    increaseResources(NVA, 9)
    if (game.trail <= 2)
      improveTrail(3 - game.trail)
  }
}
