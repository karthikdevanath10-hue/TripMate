/**
 * TripMate - Smart Trip Planner
 * Client-Side Enhancements & Interactive Helper Functions
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Budget Quick Select Chips
    const budgetInput = document.getElementById('budgetInput');
    const budgetChips = document.querySelectorAll('.budget-chip');

    if (budgetChips.length > 0 && budgetInput) {
        budgetChips.forEach(chip => {
            chip.addEventListener('click', () => {
                budgetChips.forEach(c => c.classList.remove('active'));
                chip.classList.add('active');
                const amount = chip.getAttribute('data-amount');
                budgetInput.value = amount;
                updatePerPersonBudgetBadge();
            });
        });

        budgetInput.addEventListener('input', () => {
            budgetChips.forEach(c => {
                if (c.getAttribute('data-amount') === budgetInput.value) {
                    c.classList.add('active');
                } else {
                    c.classList.remove('active');
                }
            });
            updatePerPersonBudgetBadge();
        });
    }

    // 2. Dynamic Group Members Fields
    const numTravellersSelect = document.getElementById('numberOfTravellers');
    const groupMembersContainer = document.getElementById('groupMembersList');

    if (numTravellersSelect && groupMembersContainer) {
        numTravellersSelect.addEventListener('change', renderGroupMemberInputs);
        renderGroupMemberInputs(); // initial render
    }

    function renderGroupMemberInputs() {
        if (!numTravellersSelect || !groupMembersContainer) return;
        const count = parseInt(numTravellersSelect.value, 10) || 1;
        const membersNeeded = count - 1; // Leader is member 1

        groupMembersContainer.innerHTML = '';

        if (membersNeeded <= 0) {
            return;
        }

        for (let i = 1; i <= membersNeeded; i++) {
            const memberRow = document.createElement('div');
            memberRow.className = 'row g-2 mb-3 p-3 bg-light rounded-3 border';
            memberRow.innerHTML = `
                <div class="col-12 text-muted fw-bold small mb-1">
                    <i class="fa-solid fa-user-plus me-1 text-primary"></i> Traveller #${i + 1}
                </div>
                <div class="col-md-8">
                    <label class="form-label small">Member Name</label>
                    <input type="text" name="memberNames" class="form-control" placeholder="Full name of traveller ${i + 1}" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label small">Age</label>
                    <input type="number" name="memberAges" class="form-control" min="1" max="120" placeholder="Age" required>
                </div>
            `;
            groupMembersContainer.appendChild(memberRow);
        }
        updatePerPersonBudgetBadge();
    }

    function updatePerPersonBudgetBadge() {
        const badge = document.getElementById('perPersonBudgetBadge');
        if (!badge || !budgetInput) return;
        const budget = parseFloat(budgetInput.value) || 0;
        let count = 1;
        if (numTravellersSelect) {
            count = parseInt(numTravellersSelect.value, 10) || 1;
        }
        const perPerson = Math.round(budget / count);
        badge.textContent = `₹${perPerson.toLocaleString('en-IN')} / person`;
    }

    // 3. Image Fallback Handler for remote images
    const images = document.querySelectorAll('img');
    images.forEach(img => {
        img.addEventListener('error', () => {
            img.src = 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?auto=format&fit=crop&w=800&q=80';
        });
    });
});

/**
 * Demo Payment Simulator: One-click autofill for college project demonstration
 */
function simulateDemoPayment(sampleId) {
    const input = document.getElementById('transactionIdInput');
    if (input) {
        input.value = sampleId || ('UPI/DEMO/' + Math.floor(10000000 + Math.random() * 90000000));
        input.classList.add('is-valid');
    }
}

/**
 * Print booking voucher / download payment receipt
 */
function printVoucher() {
    window.print();
}

/**
 * Download Trip Plan PDF directly on the itinerary page
 */
function downloadTripPlanPdf() {
    window.print();
}

/**
 * Open and download the actual trip plan / itinerary as PDF from the payment page
 */
function downloadItineraryPdf(destId) {
    if (!destId) {
        window.print();
        return;
    }
    const printUrl = '/itinerary/' + destId + '?print=true';
    const win = window.open(printUrl, '_blank');
    if (win) {
        win.focus();
    }
}

// Auto-trigger print dialog if page was opened with ?print=true (for itinerary PDF generation)
if (typeof window !== 'undefined' && window.location && window.location.search && window.location.search.includes('print=true')) {
    window.addEventListener('load', () => {
        setTimeout(() => {
            window.print();
        }, 500);
    });
}


